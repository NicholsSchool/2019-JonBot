/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.StringJoiner;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;


public class IndependentDriveTrain extends RobotDriveBase {
    public static final double kDefaultQuickStopThreshold = 0.2;
    public static final double kDefaultQuickStopAlpha = 0.1;

    private static int instances;

    private final SpeedController m_leftMotor;
    private final SpeedController m_rightMotor;

    private double m_quickStopThreshold = kDefaultQuickStopThreshold;
    private double m_quickStopAlpha = kDefaultQuickStopAlpha;
    private double m_quickStopAccumulator;
    private double m_rightSideInvertMultiplier = -1.0;
    private boolean m_reported;


    private WPI_TalonSRX[] motors;
    private double[] desiredSpeeds;
    private double[] currentSpeeds;
    /**
     * Construct a DifferentialDrive.
     *
     * <p>
     * To pass multiple motors per side, use a {@link SpeedControllerGroup}. If a
     * motor needs to be inverted, do so before passing it in.
     */
    public IndependentDriveTrain(SpeedController leftMotor, SpeedController rightMotor) {
        verify(leftMotor, rightMotor);
        m_leftMotor = leftMotor;
        m_rightMotor = rightMotor;
        addChild(m_leftMotor);
        addChild(m_rightMotor);
        instances++;
        setName("DifferentialDrive", instances);
        motors = new WPI_TalonSRX[4];
        motors[0] = RobotMap.lFMaster;
        motors[1] = RobotMap.lBMaster;
        motors[2] = RobotMap.rFMaster;
        motors[3] = RobotMap.rBMaster;
        reset();
    }

    public void reset() {
        desiredSpeeds = new double[2];
        currentSpeeds = new double[2];
    }

    /**
     * Verifies that all motors are nonnull, throwing a NullPointerException if any
     * of them are. The exception's error message will specify all null motors, e.g.
     * {@code
     * NullPointerException("leftMotor, rightMotor")}, to give as much information
     * as possible to the programmer.
     *
     * @throws NullPointerException if any of the given motors are null
     */
    @SuppressWarnings("PMD.AvoidThrowingNullPointerException")
    private void verify(SpeedController leftMotor, SpeedController rightMotor) {
        if (leftMotor != null && rightMotor != null) {
            return;
        }
        StringJoiner joiner = new StringJoiner(", ");
        if (leftMotor == null) {
            joiner.add("leftMotor");
        }
        if (rightMotor == null) {
            joiner.add("rightMotor");
        }
        throw new NullPointerException(joiner.toString());
    }

    /**
     * Arcade drive method for differential drive platform. The calculated values
     * will be squared to decrease sensitivity at low speeds.
     *
     * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is
     *                  positive.
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0].
     *                  Clockwise is positive.
     */
    @SuppressWarnings("ParameterName")
    public void arcadeDrive(double xSpeed, double zRotation) {
        arcadeDrive(xSpeed, zRotation, true);
    }

    /**
     * Arcade drive method for differential drive platform.
     *
     * @param xSpeed       The robot's speed along the X axis [-1.0..1.0]. Forward
     *                     is positive.
     * @param zRotation    The robot's rotation rate around the Z axis [-1.0..1.0].
     *                     Clockwise is positive.
     * @param squareInputs If set, decreases the input sensitivity at low speeds.
     */
    @SuppressWarnings("ParameterName")
    public void arcadeDrive(double xSpeed, double zRotation, boolean squareInputs) {
        if (!m_reported) {
            HAL.report(tResourceType.kResourceType_RobotDrive, 2, tInstances.kRobotDrive2_DifferentialArcade);
            m_reported = true;
        }

        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squareInputs) {
            xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
            zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }

        double leftMotorOutput;
        double rightMotorOutput;

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            } else {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }

        m_leftMotor.set(limit(leftMotorOutput) * m_maxOutput);
        m_rightMotor.set(limit(rightMotorOutput) * m_maxOutput * m_rightSideInvertMultiplier);

        feed();
    }

    /**
     * Curvature drive method for differential drive platform.
     *
     * <p>
     * The rotation argument controls the curvature of the robot's path rather than
     * its rate of heading change. This makes the robot more controllable at high
     * speeds. Also handles the robot's quick turn functionality - "quick turn"
     * overrides constant-curvature turning for turn-in-place maneuvers.
     *
     * @param xSpeed      The robot's speed along the X axis [-1.0..1.0]. Forward is
     *                    positive.
     * @param zRotation   The robot's rotation rate around the Z axis [-1.0..1.0].
     *                    Clockwise is positive.
     * @param isQuickTurn If set, overrides constant-curvature turning for
     *                    turn-in-place maneuvers.
     */
    @SuppressWarnings({ "ParameterName", "PMD.CyclomaticComplexity" })
    public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
        if (!m_reported) {
            HAL.report(tResourceType.kResourceType_RobotDrive, 2, tInstances.kRobotDrive2_DifferentialCurvature);
            m_reported = true;
        }

        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        double angularPower;
        boolean overPower;

        if (isQuickTurn) {
            if (Math.abs(xSpeed) < m_quickStopThreshold) {
                m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
                        + m_quickStopAlpha * limit(zRotation) * 2;
            }
            overPower = true;
            angularPower = zRotation;
        } else {
            overPower = false;
            angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

            if (m_quickStopAccumulator > 1) {
                m_quickStopAccumulator -= 1;
            } else if (m_quickStopAccumulator < -1) {
                m_quickStopAccumulator += 1;
            } else {
                m_quickStopAccumulator = 0.0;
            }
        }

        double leftMotorOutput = xSpeed + angularPower;
        double rightMotorOutput = xSpeed - angularPower;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            if (leftMotorOutput > 1.0) {
                rightMotorOutput -= leftMotorOutput - 1.0;
                leftMotorOutput = 1.0;
            } else if (rightMotorOutput > 1.0) {
                leftMotorOutput -= rightMotorOutput - 1.0;
                rightMotorOutput = 1.0;
            } else if (leftMotorOutput < -1.0) {
                rightMotorOutput -= leftMotorOutput + 1.0;
                leftMotorOutput = -1.0;
            } else if (rightMotorOutput < -1.0) {
                leftMotorOutput -= rightMotorOutput + 1.0;
                rightMotorOutput = -1.0;
            }
        }

        // Normalize the wheel speeds
        double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
        if (maxMagnitude > 1.0) {
            leftMotorOutput /= maxMagnitude;
            rightMotorOutput /= maxMagnitude;
        }

        m_leftMotor.set(leftMotorOutput * m_maxOutput);
        m_rightMotor.set(rightMotorOutput * m_maxOutput * m_rightSideInvertMultiplier);

        feed();
    }

    public void set(double speed) {

        for (int i = 0; i < motors.length; i++) {
            int direction = 1;
            if (i < 2)
                direction *= -1;

            motors[i].set(speed * direction);
        }
     //   currentSpeed = speed;
    }

    public void sigmoidDrive() {
        sigmoidMove(-Robot.oi.j0.getY(), -Robot.oi.j1.getY());
    }

    public void sigmoidMove(double leftSpeed, double rightSpeed) {
        double left = sigmoidSideMove(leftSpeed, false);
        double right = sigmoidSideMove(rightSpeed, true);
        System.out.println("Sigmoid moving");
        tankDrive(left, right); //Test to see if the movement is smooth, 
                                //then try changing the use of speed controller groups
                                // to each motor
    }

    private double sigmoidSideMove(double speed, boolean isRight) {
        int index = 0;
        if (isRight)
            index = 1;

        if (speed > 0.99 || speed < -0.99)
            speed = 0.99 * speed;

        desiredSpeeds[index] = speed;
        if (Math.abs(desiredSpeeds[index] - currentSpeeds[index]) < 0.01) {
            return desiredSpeeds[index];
        }

        double startTime = inverseSig(currentSpeeds[index]);
        double cycleTime = 0.02;

        if (desiredSpeeds[index] < currentSpeeds[index])
            cycleTime = -cycleTime;

        double sigSpeed = sigmoid(startTime + cycleTime);

        return sigSpeed;
    }

    private double sigmoid(double time) {
        return 2 / (1 + Math.pow(Math.E, -time * Constants.SIGMOID_A)) - 1;
    }

    private double inverseSig(double speed) {
        return -Math.log(2 / (speed + 1) - 1) / Constants.SIGMOID_A;
    }

    /**
     * Tank drive method for differential drive platform. The calculated values will
     * be squared to decrease sensitivity at low speeds.
     *
     * @param leftSpeed  The robot's left side speed along the X axis [-1.0..1.0].
     *                   Forward is positive.
     * @param rightSpeed The robot's right side speed along the X axis [-1.0..1.0].
     *                   Forward is positive.
     */
    public void tankDrive(double leftSpeed, double rightSpeed) {
        tankDrive(leftSpeed, rightSpeed, true);
    }

    /**
     * Tank drive method for differential drive platform.
     *
     * @param leftSpeed    The robot left side's speed along the X axis [-1.0..1.0].
     *                     Forward is positive.
     * @param rightSpeed   The robot right side's speed along the X axis
     *                     [-1.0..1.0]. Forward is positive.
     * @param squareInputs If set, decreases the input sensitivity at low speeds.
     */
    public void tankDrive(double leftSpeed, double rightSpeed, boolean squareInputs) {
        if (!m_reported) {
            HAL.report(tResourceType.kResourceType_RobotDrive, 2, tInstances.kRobotDrive2_DifferentialTank);
            m_reported = true;
        }

        leftSpeed = limit(leftSpeed);
        leftSpeed = applyDeadband(leftSpeed, m_deadband);

        rightSpeed = limit(rightSpeed);
        rightSpeed = applyDeadband(rightSpeed, m_deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squareInputs) {
            leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
            rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
        }
        System.out.println("leftSpeed" + leftSpeed);
        m_leftMotor.set(leftSpeed * m_maxOutput);
        m_rightMotor.set(rightSpeed * m_maxOutput * m_rightSideInvertMultiplier);

        feed();
    }

    /**
     * Sets the QuickStop speed threshold in curvature drive.
     *
     * <p>
     * QuickStop compensates for the robot's moment of inertia when stopping after a
     * QuickTurn.
     *
     * <p>
     * While QuickTurn is enabled, the QuickStop accumulator takes on the rotation
     * rate value outputted by the low-pass filter when the robot's speed along the
     * X axis is below the threshold. When QuickTurn is disabled, the accumulator's
     * value is applied against the computed angular power request to slow the
     * robot's rotation.
     *
     * @param threshold X speed below which quick stop accumulator will receive
     *                  rotation rate values [0..1.0].
     */
    public void setQuickStopThreshold(double threshold) {
        m_quickStopThreshold = threshold;
    }

    /**
     * Sets the low-pass filter gain for QuickStop in curvature drive.
     *
     * <p>
     * The low-pass filter filters incoming rotation rate commands to smooth out
     * high frequency changes.
     *
     * @param alpha Low-pass filter gain [0.0..2.0]. Smaller values result in slower
     *              output changes. Values between 1.0 and 2.0 result in output
     *              oscillation. Values below 0.0 and above 2.0 are unstable.
     */
    public void setQuickStopAlpha(double alpha) {
        m_quickStopAlpha = alpha;
    }

    /**
     * Gets if the power sent to the right side of the drivetrain is multipled by
     * -1.
     *
     * @return true if the right side is inverted
     */
    public boolean isRightSideInverted() {
        return m_rightSideInvertMultiplier == -1.0;
    }

    /**
     * Sets if the power sent to the right side of the drivetrain should be
     * multipled by -1.
     *
     * @param rightSideInverted true if right side power should be multipled by -1
     */
    public void setRightSideInverted(boolean rightSideInverted) {
        m_rightSideInvertMultiplier = rightSideInverted ? -1.0 : 1.0;
    }

    @Override
    public void stopMotor() {
        m_leftMotor.stopMotor();
        m_rightMotor.stopMotor();
        feed();
    }

    @Override
    public String getDescription() {
        return "DifferentialDrive";
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.setActuator(true);
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Left Motor Speed", m_leftMotor::get, m_leftMotor::set);
        builder.addDoubleProperty("Right Motor Speed", () -> m_rightMotor.get() * m_rightSideInvertMultiplier,
                x -> m_rightMotor.set(x * m_rightSideInvertMultiplier));
    }
}

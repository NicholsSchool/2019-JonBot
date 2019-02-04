package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.TankDrive;
import frc.robot.Constants;
import frc.robot.Robot;
public class DriveTrain extends Subsystem
{
    WPI_TalonSRX[] motors = new WPI_TalonSRX[4];
    private double[] desiredSpeeds;
    public double[] currentSpeeds;
    public DriveTrain()
    {
        motors[0] = RobotMap.lFMaster;
        motors[1] = RobotMap.lBMaster;
        motors[2] = RobotMap.rFMaster;
        motors[3] = RobotMap.rBMaster;
        reset();
        config();
    }

    private void config()
    {
        for(int i = 0; i < motors.length; i ++)
        {
            // motors[i].setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
            // Constants.kTimeoutMs);
            // motors[i].setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,
            // Constants.kTimeoutMs);

            motors[i].configNominalOutputForward(0, Constants.kTimeoutMs);
            motors[i].configNominalOutputReverse(0, Constants.kTimeoutMs);
            motors[i].configPeakOutputForward(1, Constants.kTimeoutMs);
            motors[i].configPeakOutputReverse(-1, Constants.kTimeoutMs);

            motors[i].config_kF(Constants.kPIDLoopIdx, Constants.kF,
            Constants.kTimeoutMs);
            motors[i].config_kP(Constants.kPIDLoopIdx, Constants.kP,
            Constants.kTimeoutMs);
            motors[i].config_kI(Constants.kPIDLoopIdx, Constants.kI,
            Constants.kTimeoutMs);
            motors[i].config_kD(Constants.kPIDLoopIdx, Constants.kD,
            Constants.kTimeoutMs);

            /* Set acceleration and vcruise velocity - see documentation */
        //    motors[i].configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
        //    motors[i].configMotionAcceleration(6000, Constants.kTimeoutMs);

            /* Zero the sensor */
            motors[i].setSelectedSensorPosition(0, Constants.kPIDLoopIdx,
            Constants.kTimeoutMs);
        }
    }

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new TankDrive());
    }

    public void resetEncoders()
    {
        for(int i = 0; i < motors.length; i ++)
        {
            motors[i].setSelectedSensorPosition(0);
        }
    }

    public void setDistance(double distance)
    {
        double targetPositionRotations =  distance;
        for(int i = 0; i < motors.length; i ++)
        {
            int direction = 1;
            if (i >= 2)
                direction *= -1;
            motors[i].set(ControlMode.Position, targetPositionRotations * direction);
        } 
    }
    //Not Working
    public void setVelocity(double speed)
    {
        double targetVelocity_UnitsPer100ms = speed * 500.0 * 4096 / 600;
        for(int i = 0; i < motors.length; i ++)
        {
            System.out.println("PID Looping " + i);

            //Saw that Right side was reversed, 
            //will reverse them in robot map after testing complete
            int direction =1;
            if(i >=2)
                direction *=-1;

            System.out.println("Target Velocity: " + targetVelocity_UnitsPer100ms);
            motors[i].set(ControlMode.Velocity, targetVelocity_UnitsPer100ms *direction);
        }
    }

    public void reset()
    {
        desiredSpeeds = new double[2];
        currentSpeeds = new double[2];
    }
    
    public void set(double speed)
    {
       
        for(int i = 0; i < motors.length; i ++)
        {
            int direction = 1;
            if(i < 2)
                direction*= -1;

            motors[i].set(speed * direction);
        }
    }

    public void stopMotor(int index)
    {
        motors[index].stopMotor();
    }
  
    public void sigmoidDrive()
    {
        sigmoidMove(-Robot.oi.j0.getY(), -Robot.oi.j1.getY());
    }

    public void sigmoidMove(double leftSpeed, double rightSpeed)
    {
       sigmoidMove(leftSpeed, rightSpeed, Constants.SIGMOID_A);
    }

    public void sigmoidMove(double leftSpeed, double rightSpeed, double a)
    {
        double left = sigmoidSideMove(leftSpeed, false, a);
        double right = sigmoidSideMove(rightSpeed, true, a);
        move(left, right);
    }

    private double sigmoidSideMove(double speed, boolean isRight, double a)
    {
        int index = 0;
        if(isRight)
            index = 1;

        if (speed > 0.99 || speed < -0.99)
            speed = 0.99 * speed;

        desiredSpeeds[index] = speed;
        if (Math.abs(desiredSpeeds[index] - currentSpeeds[index]) < 0.01) {
            return desiredSpeeds[index];
        }

        double startTime = inverseSig(currentSpeeds[index], a);
        double cycleTime = 0.02;

        if (desiredSpeeds[index] < currentSpeeds[index])
            cycleTime = -cycleTime;

        double sigSpeed = sigmoid(startTime + cycleTime, a);

        return sigSpeed;
    }

    private double sigmoid(double time, double a)
    {
        return 2 / (1 + Math.pow(Math.E, -time * a)) - 1;
    }

    private double sigmoid(double time)
    {
        return sigmoid(time, Constants.SIGMOID_A);
    }

    private double inverseSig(double speed, double a)
    {
        return -Math.log(2 / (speed + 1) - 1) / a;
    }

    private double inverseSig(double speed)
    {
        return inverseSig(speed, Constants.SIGMOID_A);
    }


    public void tankDrive()
    {
        move(-Robot.oi.j0.getY(), -Robot.oi.j1.getY());
    }

    public void move(double leftSpeed, double rightSpeed)
    {
        RobotMap.driveTank.tankDrive(leftSpeed, rightSpeed);
        currentSpeeds[0] = leftSpeed;
        currentSpeeds[1] = rightSpeed;
    }

    public void stop()
    {
        reset();
        RobotMap.driveTank.stopMotor();
    }
}
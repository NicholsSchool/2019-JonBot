/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.sensors.Vision;

public class AlignDemo extends Command {

    private double speed;
    private double angularDistance;
    private double distance;
    private double rotation;

    private boolean isFacingLine;
    private boolean isOnLine;
    private boolean isAligned;

    public static final double DISTANCE_ERROR = 0.8;

    public AlignDemo(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.driveTrain);
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        isFacingLine = false;
        isOnLine = false;
        isAligned = false;

        System.out.println("\n\n[VISION]: Starting...\n\n");
        Robot.navX.reset();
        angularDistance = Vision.angularDistance;
        System.out.println("\n\n[VISION]: Angular Distance: " + angularDistance);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!isFacingLine) {
            if (Robot.navX.getAngle() > angularDistance + 1) {
                Robot.driveTrain.move(-speed, speed);
            } else if (Robot.navX.getAngle() < angularDistance - 1) {
                Robot.driveTrain.move(speed, -speed);
            } else {
                System.out.println("\n\n[VISION]: Faced Line\n\n");
                isFacingLine = true;

                distance = Vision.distance / Constants.WHEEL_DIAMETER / Math.PI * Constants.TICKS_PER_ROTATION * DISTANCE_ERROR;
                rotation = Vision.rotation;

                Robot.driveTrain.resetEncoders();

                System.out.println("\n\n[VISION]: Distance: " + distance + ", Rotation: " + rotation + "\n\n");
            }

            return;
        }

        if (isFacingLine && !isOnLine) {
            if (Math.abs(RobotMap.lFMaster.getSelectedSensorPosition(0)) < distance) {
                RobotMap.driveTank.tankDrive(speed, speed);
            } else {
                System.out.println("\n\n[VISION]: On Line\n\n");
                isOnLine = true;
                Robot.navX.reset();
            }

            return;
        }

        if (isFacingLine && isOnLine && !isAligned) {
            System.out.println("\n\n\n\n\nGoal: " + rotation + ", Current: " + Robot.navX.getAngle() + "\n\n\n\n\n");
            if (Robot.navX.getAngle() > rotation + 1) {
                Robot.driveTrain.move(-speed, speed);
            } else if (Robot.navX.getAngle() < rotation - 1) {
                Robot.driveTrain.move(speed, -speed);
            } else {
                System.out.println("\n\n[VISION]: Aligned\n\n");
                isAligned = true;
            }

            return;
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return isAligned;
    }

    // Called once after isFinished returns true
    @Override
    @SuppressWarnings({"resource"})
    protected void end() {
        Robot.driveTrain.stop();
        new TankDrive().start();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

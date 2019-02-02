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
    private double distance;
    private double rotation;

    private boolean isFacingLine;
    private boolean isOnLine;
    private boolean isAligned;

    private int startPosition;

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

        System.out.println("Starting...\n\n");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!isFacingLine) {
            if (Vision.x > 0.55) {
                Robot.driveTrain.move(speed, -speed);
            } else if (Vision.x < 0.45) {
                Robot.driveTrain.move(-speed, speed);
            } else {
                System.out.println("Faced Line");
                isFacingLine = true;

                distance = Vision.distance / Constants.WHEEL_DIAMETER / Math.PI * Constants.TICKS_PER_ROTATION;
                rotation = Vision.rotation;

                Robot.driveTrain.resetEncoders();

                System.out.println("\n\n\n\nDistance: " + distance + ", Rotation: " + rotation + "\n\n\n\n");
            }
        }

        if (isFacingLine && !isOnLine) {
            System.out.println("Goal: " + startPosition + distance + " Current Position: "
                    + RobotMap.lFMaster.getSelectedSensorPosition(0));
            if (Math.abs(RobotMap.lFMaster.getSelectedSensorPosition(0)) < distance) {
                RobotMap.driveTank.tankDrive(speed, speed);
            } else {
                System.out.println("On Line");
                isOnLine = true;

                Robot.navX.reset();
            }
        }

        if (isFacingLine && isOnLine && !isAligned) {
            if (Robot.navX.getAngle() > rotation + 5) {
                Robot.driveTrain.move(-speed, speed);
            } else if (Robot.navX.getAngle() < rotation - 5) {
                Robot.driveTrain.move(speed, -speed);
            } else {
                System.out.println("Aligned");
                isAligned = true;
            }
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return isAligned;
    }

    // Called once after isFinished returns true
    @Override
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

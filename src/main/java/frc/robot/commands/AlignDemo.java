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
    private double angleToLine;
    private double distanceToLine;
    private double angleToWall;

    private boolean isFacingLine;
    private boolean isOnLine;
    private boolean isAligned;

    public static final int ANGLE_BUFFER = 4;

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
        angleToLine = Vision.angleToLine;
        distanceToLine = Vision.distanceToLine * 12 / Constants.WHEEL_DIAMETER / Math.PI * Constants.TICKS_PER_ROTATION;
        angleToWall = Vision.angleToWall - Vision.angleToLine;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!isFacingLine) {

            if (Robot.navX.getAngle() > angleToLine + ANGLE_BUFFER) {
                Robot.driveTrain.move(-speed, speed);
            } else if (Robot.navX.getAngle() < angleToLine - ANGLE_BUFFER) {
                Robot.driveTrain.move(speed, -speed);
            } else {
                System.out.println("\n\n[VISION]: Faced Line\n\n");
                isFacingLine = true;

                Robot.driveTrain.resetEncoders();
            }

        } else if (!isOnLine) {

            if (Math.abs(RobotMap.lFMaster.getSelectedSensorPosition(0)) < distanceToLine) {
                RobotMap.driveTank.tankDrive(speed, speed);
            } else {
                System.out.println("\n\n[VISION]: On Line\n\n");
                isOnLine = true;
                Robot.navX.reset();
            }

        } else if (!isAligned) {

            if (Robot.navX.getAngle() > angleToWall + ANGLE_BUFFER) {
                Robot.driveTrain.move(-speed, speed);
            } else if (Robot.navX.getAngle() < angleToWall - ANGLE_BUFFER) {
                Robot.driveTrain.move(speed, -speed);
            } else {
                System.out.println("\n\n[VISION]: Aligned\n\n");
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

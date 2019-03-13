/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class WaypointAlign extends Command {
    private double piAngleToLine;
    private double piDistanceToLine;
    private double piAngleToWall;

    private double speed;

    private double angleToLine;
    private double distanceToLine;
    private double angleToWall;

    private boolean isFacingLine;
    private boolean isOnLine;
    private boolean isAligned;

    public static final int ANGLE_BUFFER = 4;

    public WaypointAlign(int waypoint, double speed) {
        requires(Robot.driveTrain);

        NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
        int flags = EntryListenerFlags.kNew | EntryListenerFlags.kUpdate;

        table.getEntry("angleToLine" + waypoint).addListener(event -> {
            piAngleToLine = event.value.getDouble();
        }, flags);
        table.getEntry("distanceToLine" + waypoint).addListener(event -> {
            piDistanceToLine = event.value.getDouble();
        }, flags);
        table.getEntry("angleToWall" + waypoint).addListener(event -> {
            piAngleToWall = event.value.getDouble();
        }, flags);

        this.speed = speed;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        isFacingLine = false;
        isOnLine = false;
        isAligned = false;

        Robot.navX.reset();

        angleToLine = piAngleToLine;
        distanceToLine = piDistanceToLine * 12 / Constants.WHEEL_DIAMETER / Math.PI * Constants.TICKS_PER_ROTATION;
        angleToWall = piAngleToWall - piAngleToLine;
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

    
    // Gets a smoothed motor value from 0 to 1 based on a x value from 0 to 1
    private double sigmoidProfile(double x) {
        x = Math.abs(x);
        x += 0.05;

        if(x < 0 || x > 1) {
            return 0;
        }

        final double a = 5;

        if(x < 0.5) {
            return 2 / (1 + Math.pow(Math.E, -a * x)) - 1;
        } else {
            return 2 / (1 + Math.pow(Math.E, a * x - a)) - 1;
        }
    }
}

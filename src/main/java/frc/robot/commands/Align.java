/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Align extends Command {

    private double lineX;
    private double lineY;

    private long lastUpdate;

    public Align() {
        requires(Robot.driveTrain);

        lineX = 0;
        lineY = 0;
        lastUpdate = 0;

        NetworkTableInstance.getDefault().getTable("pi").getEntry("lineX").addListener(event -> {

            lineX = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();

        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        NetworkTableInstance.getDefault().getTable("pi").getEntry("linYe").addListener(event -> {

            lineY = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();

        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {



    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        System.out.println("LineX: " + lineX);
        System.out.println("LineY: " + lineY);


        if(lineX != 0 && System.currentTimeMillis() - lastUpdate < 1000) {
            if(lineX < 0.5) {
                Robot.driveTrain.move(-0.5, 0.5);
            } else {
                Robot.driveTrain.move(0.5, -0.5);
            }
        } else {
            System.out.println("Something went wrong! Was the information too old?");
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
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

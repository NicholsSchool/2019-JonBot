/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.sensors.Vision;

public class RotateDemo extends Command {
    private double speed;
    private double rotation;

    private boolean isAligned;

    public RotateDemo(double speed) {
        requires(Robot.driveTrain);
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        isAligned = false;
        rotation = Vision.angleToWall;
        Robot.navX.reset();
        System.out.println("\n\n\n\n");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        System.out.println("Goal: " + rotation + ", current: " + Robot.navX.getAngle());
        if (Robot.navX.getAngle() > rotation + 1) {
            Robot.driveTrain.move(-speed, speed);
        } else if (Robot.navX.getAngle() < rotation - 1) {
            Robot.driveTrain.move(speed, -speed);
        } else {
            System.out.println("\n\n[VISION]: Aligned\n\n");
            isAligned = true;
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

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
import frc.robot.Robot;

public class AlignDemo extends Command {

    private static final double SPEED = 0.5;
    private static final long TIMEOUT = 500;
    private static final double THRESHOLD_X = 0.05, THRESHOLD_Y = 0.75;
    private static final double JOYSTICK_THRESHOLD = 0.1;

    private double x;
    private double y;
    private double vx;
    private double vy;
    private long lastUpdate;

    private NetworkTable table;

    private boolean onLine;
    private double angle;

    public AlignDemo() {
        requires(Robot.driveTrain);

        x = 0;
        y = 0;
        vx = 0;
        vy = 0;
        lastUpdate = 0;

        table = NetworkTableInstance.getDefault().getTable("vision");

        table.getEntry("x").addListener(event -> {
            x = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        table.getEntry("y").addListener(event -> {
            y = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        table.getEntry("vx").addListener(event -> {
            vx = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        table.getEntry("vy").addListener(event -> {
            vy = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("Aligning....");
        onLine = false;
        angle = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

        if (System.currentTimeMillis() - lastUpdate < TIMEOUT) {

            // Turn to the line from far away
            if (x > 0.5 + THRESHOLD_X) {
                Robot.driveTrain.move(SPEED, -SPEED);
                angle = Math.atan(vy / vx);
            } else if (x < 0.5 - THRESHOLD_X) {
                Robot.driveTrain.move(-SPEED, SPEED);
                angle = Math.atan(vy / vx);
            } else {

                // Move forward to the line
                if (y < THRESHOLD_Y) {
                    Robot.driveTrain.move(SPEED, SPEED);
                } else {
                    onLine = true;
                }
            }
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        boolean joystickMoved = Math.abs(Robot.oi.j0.getY()) > JOYSTICK_THRESHOLD
                || Math.abs(Robot.oi.j1.getY()) > JOYSTICK_THRESHOLD;
        return joystickMoved || onLine;
    }

    // Called once after isFinished returns true
    @Override
    @SuppressWarnings({"resource"})
    protected void end() {
        Robot.driveTrain.stop();
        if (onLine) {
            // Align with the line while on it
            double complementary = Math.PI / 2 - Math.abs(angle);
            double fromVertical = Math.copySign(complementary, angle);
            fromVertical *= 180 / Math.PI;
            new TurnToAngle(fromVertical, 0.5).start();
        } else {
            new TankDrive().start();
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}

package frc.robot.commands;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SelfCorrect extends Command {
    double xPercent;
    double yPercent;
    boolean turnRight;
    double speed;

    public SelfCorrect() {
        requires(Robot.driveTrain);
        Robot.xEntry.addListener(event -> {
            xPercent = event.value.getDouble();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    @Override
    protected void initialize() {
        speed = 0.45;
        xPercent = Robot.xEntry.getDouble(0.5);
        yPercent = Robot.yEntry.getDouble(0.5);
        turnRight = xPercent > 0.5;
    }

    @Override
    protected void execute() {
        if (turnRight)
            Robot.driveTrain.move(speed, -speed);
        else
            Robot.driveTrain.move(-speed, speed);

    }

    @Override
    protected boolean isFinished() {
        System.out.println("xPercent: " + xPercent);
        return ((xPercent > 0.45) && (xPercent < 0.55));
    }

    @Override
    protected void end() {
        Robot.driveTrain.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
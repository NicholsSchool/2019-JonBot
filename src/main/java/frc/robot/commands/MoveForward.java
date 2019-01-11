package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class MoveForward extends Command
{
    double speed;
    double time;
    public MoveForward(double speed, double time)
    {
        requires(Robot.driveTrain);
        this.speed = speed;
        this.time = time;
    }


    protected void initialize() {
        
    }
    protected void execute() {
        Robot.driveTrain.move(speed, speed);
        System.out.println("Running Command");
    }

    @Override
    protected boolean isFinished() {
        return timeSinceInitialized() >= time;
    }

    protected void end()
    {
        Robot.driveTrain.stop();
    }
    protected void interrupted()
    {
        end();
    }
}
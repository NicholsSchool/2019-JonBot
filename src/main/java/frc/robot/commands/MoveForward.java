package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class MoveForward extends Command
{
    double speed;
    double time;

    public MoveForward(double s, double t)
    {
        requires(Robot.driveTrain);
        speed = s;
        time = t;
    }
    
    protected void initialize() 
    {
        
    }

    
    protected void execute() 
    {
      Robot.driveTrain.move(speed, speed);
      System.out.println("Running MoveForward"); 
    }


    @Override
    protected boolean isFinished() 
    {
        if (time > timeSinceInitialized())
            return false;
        return true;
        
    }
    @Override
    protected void end() 
    {
        Robot.driveTrain.stop();
    }

    @Override
    protected void interrupted() 
    {
        end();
    }
}
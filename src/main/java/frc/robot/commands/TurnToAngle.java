package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TurnToAngle extends Command
{
    double angle;
    double speed;

    public TurnToAngle(double a, double s)
    {
        angle = a;
        speed = s;
        requires (Robot.driveTrain);
    }

    @Override
    protected void initialize()
    {
        Robot.navX.reset();
    }

    @Override
    protected void execute()
    {
        if (angle > 0)
        {
            Robot.driveTrain.move(speed, -speed);
        }
        else 
        {
            Robot.driveTrain.move(-speed, speed);
        }
    }

    @Override
    protected boolean isFinished() 
    {
        
        double currentAngle = Robot.navX.getAngle();
        System.out.println("Current Angle:" + currentAngle);
        if (currentAngle < angle + 5 && currentAngle > angle - 5)
        {
            return true;
        }
            return false;
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
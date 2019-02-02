    package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TankDrive extends Command
{
    public static boolean isRunning = false;

    public TankDrive()
    {
        requires(Robot.driveTrain);
    }
    
    protected void initialize() 
    {
        isRunning = true;
    }

    
    protected void execute() {
      Robot.driveTrain.tankDrive();
    }


    @Override
    protected boolean isFinished() {
        return false;
    }
    @Override
    protected void end() {
        Robot.driveTrain.stop();
        isRunning = false;
    }

    @Override
    protected void interrupted() {
        end();
    }
}
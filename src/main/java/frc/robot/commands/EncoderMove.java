package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class EncoderMove extends Command
{
    double distance;
    double speed;
    double startPosition;
    double rotations;

    public EncoderMove(double in, double s)
    {
        
        rotations = (in/(Constants.WHEEL_DIAMETER*Math.PI));
        distance =  rotations * (Constants.TICKS_PER_ROTATION);
        speed = s;
      
        requires(Robot.driveTrain);
    }

    @Override
    protected boolean isFinished() 
    {
    if (distance > 0)
    {
    if (RobotMap.lFMaster.getSelectedSensorPosition(0) >= distance + startPosition)
        return true;
    }
    else 
    {
     if (RobotMap.lFMaster.getSelectedSensorPosition(0) <= distance + startPosition)
        return true;
    }
    return false;
    }

    @Override
    protected void initialize()
    {
        startPosition = RobotMap.lFMaster.getSelectedSensorPosition(0);
    }

    @Override
    protected void interrupted()
    {
        end();
    }

    @Override
    protected void execute()
    {
        Robot.driveTrain.move(speed, speed);
    }
    @Override
    protected void end()
    {
        Robot.driveTrain.resetEncoders();
        System.out.println("END");
        Robot.driveTrain.stop();
    }
}
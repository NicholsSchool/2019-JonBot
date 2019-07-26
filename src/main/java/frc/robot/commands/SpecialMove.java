package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class SpecialMove extends Command
{
    private double speed;
    private double distance;
    private double startValue;
    private double distanceToAccelerate;
    private boolean speedReached;

    public SpecialMove(double speed, double feet)
    {  
        requires(Robot.driveTrain);
        double rotations = (feet / (0.5 * Math.PI));
        this.distance = rotations * 1128;
        this.speed = speed;
        System.out.println("\n\nRunning Special\n\n");
    }

    protected void initialize() 
    {
        Robot.driveTrain.resetEncoders();
        Robot.driveTrain.reset();
        speedReached = false;
        startValue = RobotMap.lFMaster.getSelectedSensorPosition(0);
        distanceToAccelerate = 0;
    }
    @Override
    protected void execute() 
    {

        if(distance - RobotMap.lFMaster.getSelectedSensorPosition(0) > distanceToAccelerate)
            Robot.driveTrain.sigmoidMove(speed, speed, 1);
        else
            Robot.driveTrain.sigmoidMove(0, 0, 1); 

        if(Robot.driveTrain.currentSpeeds[0] >= speed && !speedReached)
        {
            speedReached = true;
            distanceToAccelerate = RobotMap.lFMaster.getSelectedSensorPosition(0) - startValue;
            System.out.println("\n\nDistance: " + distanceToAccelerate + "\n\n");
        }
    }
    @Override
    protected boolean isFinished() 
    {
        return distance <= RobotMap.lFMaster.getSelectedSensorPosition(0);
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
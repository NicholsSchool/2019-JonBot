package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class EncoderMove extends Command
{
    private double speed;
    private double distance;

    private double start;
    public EncoderMove(double speed, double distance)
    {
        double rotations = (distance/(6*Math.PI));
        this.distance = rotations * 1128;
        this.speed = -speed;
       
    }

    protected void initialize() {
        start = RobotMap.lFMaster.getSelectedSensorPosition(0);
    }

    @Override
    protected void execute() {
        Robot.driveTrain.move(speed, speed);
    }
    @Override
    protected boolean isFinished() {
        System.out.println(RobotMap.lFMaster.getSelectedSensorPosition(0));
        return RobotMap.lFMaster.getSelectedSensorPosition(0) >= distance + start;
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
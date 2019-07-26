package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SpecialTurn extends Command
{

    private double angle;
    private double leftSpeed;
    private double rightSpeed;
    public SpecialTurn(double angle, double speed)
    {
        requires(Robot.driveTrain);
        this.angle = angle;
        if(angle > 0)
        {
            leftSpeed = speed;
            rightSpeed = -speed;
        }
        else
        {
            leftSpeed = -speed;
            rightSpeed = speed;
        }
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.reset();
        Robot.navX.reset();
    }

    @Override
    protected void execute() {
        Robot.driveTrain.sigmoidMove(leftSpeed, rightSpeed, 1);
    }

    @Override
    protected boolean isFinished() {
        return Robot.navX.getAngle() > angle - 5 && Robot.navX.getAngle()  < angle + 5 ;
    }

    @Override
    protected void end() {
        Robot.driveTrain.sigmoidMove(0, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.*;
import frc.robot.RobotMap;
import frc.robot.Robot;
public class DriveTrain extends Subsystem
{

	@Override
    protected void initDefaultCommand() 
    {

    }
    public void tankDrive()
    {
        move(-Robot.oi.j0.getY(), -Robot.oi.j1.getY());
    }

    public void move(double leftSpeed, double rightSpeed)
    {
        RobotMap.driveTank.tankDrive(leftSpeed, rightSpeed);
    }

    public void stop()
    {
        RobotMap.driveTank.stopMotor();
    }
    public void resetEncoders()
        {
            RobotMap.lFMaster.getSelectedSensorPosition(0);
        }
    
    public void mecanumMove(double ySpeed, double xSpeed, double zRotation)
    {
        RobotMap.mecanumDrive.driveCartesian(ySpeed, xSpeed, zRotation);
    }
    public void mecanumDrive()
    {
        mecanumMove(-Robot.oi.j0.getY(), -Robot.oi.j0.getX(),-Robot.oi.j0.getZ());
    }
}
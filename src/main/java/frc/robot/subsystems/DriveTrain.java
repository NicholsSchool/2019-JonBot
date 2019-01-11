package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.Robot;
public class DriveTrain extends Subsystem
{

	@Override
	protected void initDefaultCommand() {
		
    }
    
public void setPosition(double position)
{
    RobotMap.lFMaster.config_kP(0, 0.15, 100);
    RobotMap.lFMaster.config_kI(0, 0.0, 100);
    RobotMap.lFMaster.config_kD(0, 0.0, 100);
    
    RobotMap.rFMaster.config_kP(0, 0.15, 100);
    RobotMap.rFMaster.config_kI(0, 0.0, 100);
    RobotMap.rFMaster.config_kD(0, 0.0, 100);
    
    RobotMap.lBMaster.config_kP(0, 0.15, 100);
    RobotMap.lBMaster.config_kI(0, 0.0, 100);
    RobotMap.lBMaster.config_kD(0, 0.0, 100);
    
    RobotMap.rBMaster.config_kP(0, 0.15, 100);
    RobotMap.rBMaster.config_kI(0, 0.0, 100);
    RobotMap.rBMaster.config_kD(0, 0.0, 100);
    
    RobotMap.lBMaster.set(ControlMode.Position, position);
    RobotMap.rBMaster.set(ControlMode.Position, position);
    
    RobotMap.lFMaster.set(ControlMode.Position, position);
    RobotMap.rFMaster.set(ControlMode.Position, position);
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
}
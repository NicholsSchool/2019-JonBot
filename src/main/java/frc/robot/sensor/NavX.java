package frc.robot.sensor;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import com.kauailabs.navx.frc.AHRS;
//import frc.robot.sensor.NavX;
//import sun.management.Sensor;
//import com.sun.org.apache.xalan.internal.templates.Constants;

public class NavX {
	private AHRS navX;
	
	public NavX(AHRS ahrs){
		this.navX = ahrs;
	}
	public double getYaw() {
		return navX.getYaw() % 360;
	}
	
	public double getAngle() {
		double angle = navX.getAngle();
		if(angle < -180 ){
			angle = 360 + angle;
		}
		else if (angle > 180) {
			angle = 360 - angle;
			angle *= -1;
		}
		return angle;
	}
	
	public boolean atAngle(double angle) {
		if(getAngle() < angle + 5 && getAngle() > angle -5){
			return true;
		}
			return false;
	}
	
	public void reset() {
		navX.reset();
	}
}
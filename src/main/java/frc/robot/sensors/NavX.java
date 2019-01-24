package frc.robot.sensors;

import com.kauailabs.navx.frc.AHRS;

public class NavX
{
    private AHRS navX;

    public NavX(AHRS ahrs)
    {
       this.navX = ahrs; 
    }
    public double getYaw()
    {
        return navX.getYaw() % 360;
    }
    public double getAngle()
    {
        double angle = navX.getAngle();
        if (angle < -180)
        {
            angle = 360 + angle;
        }
        else if (angle > 180)
        {
            angle = 360 - angle;
            angle *= -1;
        }
            return angle;
    }
    public boolean atAngle(double angle)
     {
        if(getAngle() < angle + 5 && getAngle() > angle -5)
        {
			return true;
		}
			return false;
	}
	
	public void reset() {
		navX.reset();
	}
}
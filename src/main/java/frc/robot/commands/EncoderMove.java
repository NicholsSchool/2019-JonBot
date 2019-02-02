package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;


public class EncoderMove extends Command {
 double speed;
 double distance;
 int startPosition;
 public EncoderMove(double s, double in){
   speed = s;
   double rotations = in/(6*Math.PI);
   distance = rotations*1313;

 }

 @Override
 protected void initialize() {
   startPosition = RobotMap.lFMaster.getSelectedSensorPosition(0);

 }

 @Override
 protected void execute() {
   Robot.driveTrain.move(speed, speed);
   System.out.println("Justin's startPosition " + startPosition);
   System.out.println("Justin's distance " + distance);
   System.out.println("Justin's encoder value " + RobotMap.lFMaster.getSelectedSensorPosition(0));
 }

 @Override
 protected boolean isFinished() {
   if (startPosition + distance <= RobotMap.lFMaster.getSelectedSensorPosition(0))
   {
     return true;
   }
   else
   {
     return false;
   }

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

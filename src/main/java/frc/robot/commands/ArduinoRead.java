/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.sensors.ScanPacket;

public class ArduinoRead extends Command {
  public ArduinoRead() {
   requires(Robot.arduino);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    /*Note to self: If making gatherSensorReading() method in arduino code
    the send method, it would be sending the data as it was collected.*/


    byte[] rawData = new byte[15];
    RobotMap.arduino.read(4, rawData.length, rawData);
    if(rawData[0] < 0)
    {
   //   System.out.println("No Data Yet");
      return;
    }
    System.out.println("Got Data");
    int[] data = new int[3];
    int dataSize = 0; 
    int start = 0;
    int end = -1;
    for(int i = 0; i < rawData.length; i ++)
    {
      if(rawData[i] == 32)
      {
        start = end + 1; 
        end = i;
        data[dataSize ++] = parse(rawData, start, end);
      }
    }
    Robot.arduino.scans.addScanPacket(new ScanPacket(data));
  }
  private int parse(byte[] data, int start, int end)
  {
    String val = "";
    for(int i = start; i < end; i ++)
      val += ((char) data[i]);
    return Integer.parseInt(val);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}

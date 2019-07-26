/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.ArduinoRead;
import frc.robot.sensors.ScanPacket;
import frc.robot.sensors.Scans;

/**
 * Add your docs here.
 */
public class Arduino extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public Scans scans;
  public Arduino()
  {
    scans = new Scans();
  }
  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
     setDefaultCommand(new ArduinoRead());
  }


    public void printReading(){
      scans.print();
  }
}

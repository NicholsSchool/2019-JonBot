/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class ArduinoSend extends Command {
  public ArduinoSend() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  byte[] writeData;
  byte[] readData;
  String output;
  @Override
  protected void initialize() {
    System.out.println("Sending to arduino");

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    writeData = new byte[1];
    writeData[0] = (byte) Robot.arudinoSend;
    boolean transfer = RobotMap.arduino.writeBulk(writeData, writeData.length);
    System.out.println("Transfer: " + transfer);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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

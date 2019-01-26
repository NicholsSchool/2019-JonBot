/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.concurrent.TimeUnit;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.DriveTrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static OI oi;
   public static DriveTrain driveTrain;

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  NetworkTableEntry entry;
  NetworkTableEntry entry2;
  double x;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    RobotMap.init();
    RobotMap.timer.start();
    driveTrain = new DriveTrain();
    // chooser.addObject("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);
    oi = new OI();
    NetworkTableInstance inst =  NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("pi"); 
    entry = table.getEntry("test"); 
    entry2 = table.getEntry("x");
    x = entry2.getDouble(0);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You
   * can use it to reset any subsystem information you want to clear when the
   * robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString code to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons to
   * the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
     * switch(autoSelected) { case "My Auto": autonomousCommand = new
     * MyAutoCommand(); break; case "Default Auto": default: autonomousCommand = new
     * ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    driveTrain.reset();
  }
  boolean buttonPressed = false;
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    SmartDashboard.putNumber("Current Time:", System.currentTimeMillis());
    double speed = oi.j0.getY();
    // RobotMap.testMotor.set(speed);
    // RobotMap.driveTank.tankDrive(speed, speed);

  //  driveTrain.tankDrive();
    SmartDashboard.putNumber("testMotor:", RobotMap.testMotor.getOutputCurrent());
    SmartDashboard.putNumber("P:", Constants.kP);
    SmartDashboard.putNumber("I:", Constants.kI);
    SmartDashboard.putNumber("D:", Constants.kD);
    //double test = 0;
/*
    entry.addListener(event ->{
      System.out.println("Value Changed: " + event.value.getValue() );
    },EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
*/
/*
    if(oi.j0.getRawButton(1))
    {
      x++;
      entry2.setDouble(x);
    }
    SmartDashboard.putNumber("Initial Value: ", entry2.getDouble(0));
    entry2.addListener(event -> {
      SmartDashboard.putNumber("Value Changed: ", event.value.getDouble());
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate); */
/*    if (oi.j0.getRawButton(7)) 
      driveTrain.setDistance(1000.0, 0);
    
    else if (oi.j0.getRawButton(8))
      driveTrain.setDistance(1000.0, 1);
    else if (oi.j0.getRawButton(9))
      driveTrain.setDistance(1000.0, 2);
    else if (oi.j0.getRawButton(10))
      driveTrain.setDistance(1000.0, 3);
    else {
      


    }

*/

    SmartDashboard.putNumber("Left Front", RobotMap.lFMaster.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Right Front", RobotMap.rFMaster.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Left Back",  RobotMap.lBMaster.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Right Back",  RobotMap.rBMaster.getSelectedSensorPosition(0));
 

    SmartDashboard.putNumber("Left Front Velocity", RobotMap.lFMaster.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Right Front Velocity",  RobotMap.rFMaster.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Left Back Velocity", RobotMap.lBMaster.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Right Back Velocity",  RobotMap.rBMaster.getSelectedSensorVelocity(0));
    /* If Talon is in position closed-loop, print some more info */
  
    SmartDashboard.putString("Line X", "" + NetworkTableInstance.getDefault().getTable("pi").getEntry("lineX").getDouble(0));
    SmartDashboard.putString("Line Y", "" + NetworkTableInstance.getDefault().getTable("pi").getEntry("lineY").getDouble(0));
    RobotMap.timer.reset();
    driveTrain.sigmoidMove(speed);
    SmartDashboard.putNumber("DriveTrain speed: ", driveTrain.currentSpeed);
  // driveTrain.tankDrive();

    /*else {
      System.out.println("Normal");
      RobotMap.testMotor.set(ControlMode.PercentOutput, speed);
    }   */
    /*
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (Exception e) {
      /// Do Nothing
      }*/
      
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
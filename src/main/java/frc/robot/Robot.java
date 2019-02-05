/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.TankDrive;
import frc.robot.sensors.NavX;
import frc.robot.sensors.Vision;
import frc.robot.subsystems.DriveTrain;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot 
{
  public static OI oi;
   public static DriveTrain driveTrain;
   public static NavX navX;
  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  public static NetworkTableEntry entry;
  public static NetworkTableEntry xEntry;
  public static NetworkTableEntry yEntry;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() 
  {
    RobotMap.init();
    NetworkTableInstance inst =  NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("pi"); 
    entry = table.getEntry("feet");
    xEntry = table.getEntry("xPercent");
    yEntry = table.getEntry("yPercent");
    navX = new NavX(RobotMap.ahrs);
    driveTrain = new DriveTrain();
    // chooser.addObject("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);
    oi = new OI();
    Vision.init();
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
  public void robotPeriodic() 
  {
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You
   * can use it to reset any subsystem information you want to clear when the
   * robot is disabled.
   */
  @Override
  public void disabledInit() 
  {
  }

  @Override
  public void disabledPeriodic() 
  {
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
  public void autonomousInit() 
  {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
     * switch(autoSelected) { case "My Auto": autonomousCommand = new
     * MyAutoCommand(); break; case "Default Auto": default: autonomousCommand = new
     * ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) 
    {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() 
  {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() 
  {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) 
    {
      m_autonomousCommand.cancel();
    }

    // new AlignDemo().start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() 
  {
    Scheduler.getInstance().run();
    //SmartDashboard.putNumber("Current Time:", System.currentTimeMillis());
   // RobotMap.lFMaster.set(speed);
    SmartDashboard.putNumber("Encoder Position:", RobotMap.lFMaster.getSelectedSensorPosition(0));
    // RobotMap.testMotor.set(speed);
    //RobotMap.driveTank.tankDrive(speed, speed);

    //driveTrain.tankDrive();
    //driveTrain.mecanumDrive();
    SmartDashboard.putNumber("testMotor:", RobotMap.testMotor.getOutputCurrent());
 //   System.out.println("PI Value: " + entry.getDouble(0) );

/*
    if (oi.j0.getRawButton(1)) {
      System.out.println("PID Looping");
      double targetVelocity_UnitsPer100ms = speed * 500.0 * 4096 / 600;
    //   500 RPM in either direction 
      RobotMap.testMotor.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
    } else {
      System.out.println("Normal");
      RobotMap.testMotor.set(ControlMode.PercentOutput, speed);
    }  */

    if (oi.j0.getRawButton(1))
    {
     //driveTrain.mecanumDrive();
    }
    else
    {
     //driveTrain.tankDrive();
    }

    // Driver manual override
    if((Math.abs(oi.j0.getY()) > 0.5 || Math.abs(oi.j1.getY()) > 0.5) && !TankDrive.isRunning) {
        System.out.println("Starting a new tank drive command");
        new TankDrive().start();
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
  }
}
/*--------------------------------------------------------
--------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  public static WPI_TalonSRX testMotor;

  public static WPI_TalonSRX lFMaster;
  public static WPI_TalonSRX lFSlave;

  public static WPI_TalonSRX rFMaster;
  public static WPI_TalonSRX rFSlave;

  public static WPI_TalonSRX lBMaster;
  public static WPI_TalonSRX lBSlave;

  public static WPI_TalonSRX rBMaster;
  public static WPI_TalonSRX rBSlave;

  public static SpeedControllerGroup leftMaster;
  public static SpeedControllerGroup rightMaster;

  public static DifferentialDrive driveTank;

public static AHRS ahrs;

  public static void init() {
    testMotor = new WPI_TalonSRX(Constants.MOTOR1VAL);

    /* Config sensor used for Primary PID [Velocity] */
    testMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);

    /**
     * Phase sensor accordingly. Positive Sensor Reading should match Green
     * (blinking) Leds on Talon
     */
    testMotor.setSensorPhase(true);

    /* Config the peak and nominal outputs */
    testMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
    testMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
    testMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
    testMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /* Config the Velocity closed loop gains in slot0 */
    testMotor.config_kF(Constants.kPIDLoopIdx, Constants.kF, Constants.kTimeoutMs);
    testMotor.config_kP(Constants.kPIDLoopIdx, Constants.kP, Constants.kTimeoutMs);
    testMotor.config_kI(Constants.kPIDLoopIdx, Constants.kI, Constants.kTimeoutMs);
    testMotor.config_kD(Constants.kPIDLoopIdx, Constants.kD, Constants.kTimeoutMs);

    lFMaster = new WPI_TalonSRX(Constants.LFMASTER_ID);
    lFMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 200);
    lFSlave = new WPI_TalonSRX(Constants.LFSLAVE_ID);

    rFMaster = new WPI_TalonSRX(Constants.RFMASTER_ID);
    rFSlave = new WPI_TalonSRX(Constants.RFSLAVE_ID);

    lBMaster = new WPI_TalonSRX(Constants.LBMASTER_ID);
    lBSlave = new WPI_TalonSRX(Constants.LBSLAVE_ID);
    rBMaster = new WPI_TalonSRX(Constants.RBMASTER_ID);
    rBSlave = new WPI_TalonSRX(Constants.RBSLAVE_ID);

    lFSlave.set(ControlMode.Follower, Constants.LFMASTER_ID);
    rFSlave.set(ControlMode.Follower, Constants.RFMASTER_ID);
    lBSlave.set(ControlMode.Follower, Constants.LBMASTER_ID);
    rBSlave.set(ControlMode.Follower, Constants.RBMASTER_ID);

    leftMaster = new SpeedControllerGroup(lFMaster, lBMaster);
    rightMaster = new SpeedControllerGroup(rFMaster, rBMaster);

    driveTank = new DifferentialDrive(leftMaster, rightMaster);
    ahrs = new AHRS(SPI.Port.kMXP);
  }
}
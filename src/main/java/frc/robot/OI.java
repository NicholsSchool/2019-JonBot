/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public Joystick j0;
  public Joystick j1;
  public Joystick xbox0;

  public JoystickButton j1b8;
  public JoystickButton j1b9;
  public JoystickButton j1b12;
  public JoystickButton j1b1;
  public JoystickButton j1b2;
  public OI() {
    j0 = new Joystick(0);
    j1 = new Joystick(1);
    xbox0 = new Joystick(2);

    j1b8 = new JoystickButton(j1, 8);
    j1b9 = new JoystickButton(j1, 9);
    j1b12 = new JoystickButton(j1, 12);
    
    j1b1 = new JoystickButton(j1, 1);
    j1b2 = new JoystickButton(j1, 2);
    j1b1.whenPressed(new ArduinoSend());
    j1b2.whenPressed(new ArduinoPrint());
    j1b8.whenPressed(new AdjustCommand());

    // j1b8.whenPressed(new SpecialMove(0.75, 10));
    // j1b9.whenPressed(new SpecialTurn(90, 0.5));
    // j1b12.whenPressed(new VisionCommandGroup(0.5));
  }

}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.AutoPaths;
import frc.robot.commands.EncoderMove;
import frc.robot.commands.MoveForward;
import frc.robot.commands.TurnToAngle;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
 
  public Joystick j0;
  public Joystick j1;

  public JoystickButton j1b8;
  public JoystickButton j0b8;
  public JoystickButton j1b3;
  public JoystickButton j1b7;
  public JoystickButton j1b6; 
  public JoystickButton j0b5;
  public JoystickButton j0b4;

  public OI() 
  {
    j0 = new Joystick(0);
    j1 = new Joystick(1);

    j1b8 = new JoystickButton(j1, 8);
    j0b8 = new JoystickButton(j0, 8);
    j1b3 = new JoystickButton(j1, 3);
    j1b7 = new JoystickButton(j1, 7);
    j1b6 = new JoystickButton(j1, 6);
    j0b5 = new JoystickButton(j0, 5);
    j0b4 = new JoystickButton(j0, 4);

    // j1b8.whenPressed(new MoveForward(0.5, 10));
    j0b8.whenPressed(new MoveForward(0.5, 2));

    j1b3.whenPressed(new EncoderMove(12, 0.5));
    j0b4.whenPressed(new EncoderMove(-12, -0.5));

    j1b7.whenPressed(new TurnToAngle(90, 0.5));
    j1b6.whenPressed(new TurnToAngle(-90, 0.5));

    j0b5.whenPressed(new AutoPaths());
  }
}
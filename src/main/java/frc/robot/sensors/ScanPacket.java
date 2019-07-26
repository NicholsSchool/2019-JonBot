/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

/**
 * Add your docs here.
 */
public class ScanPacket {

    private int angle, distance, strength;

    public ScanPacket(int[] values)
    {
        setUp(values);
    }


    private void setUp(int[] values)
    {
        if(values.length != 3)
            return;
        angle = values[0];
        distance = values[1];
        strength = values[2];
    }

    public int getAngle()
    {
        return angle;
    }
    public int getDistance()
    {
        return distance;
    }

    public int getStrength()
    {
        return strength;
    }

    public boolean isEmpty() {
        return angle == 0 && distance == 0 && strength == 0;
    }
    @Override
    public String toString()
    {
        return "Angle: " + angle  + "  Distance: " + distance + "  Strength" + strength;
    }
}


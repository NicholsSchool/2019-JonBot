/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class Vision {

    public static final int TIMEOUT = 500;

    public static double angularDistance;
    public static double distance;
    public static double rotation;

    public static long lastUpdate;

    public static void init() {
        // Default values
        angularDistance = -1;
        distance = -1;
        rotation = -1;
        lastUpdate = -1;

        NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
        int flags = EntryListenerFlags.kNew | EntryListenerFlags.kUpdate;

        table.getEntry("angularDistance").addListener(event -> {
            angularDistance = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, flags);
        table.getEntry("distance").addListener(event -> {
            distance = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, flags);
        table.getEntry("rotation").addListener(event -> {
            rotation = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, flags);
    }

    public static boolean isTimedOut() {
        return System.currentTimeMillis() - lastUpdate > TIMEOUT;
    }

}

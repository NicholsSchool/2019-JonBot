package frc.robot.sensors;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


public class Vision {

    public static final int TIMEOUT = 500;

    public static double angleToLine;
    public static double distanceToLine;
    public static double angleToWall;

    public static long lastUpdate;

    public static void init() {
        // Default values
        angleToLine = -1;
        distanceToLine = -1;
        angleToWall = -1;
        lastUpdate = -1;

        NetworkTable table = NetworkTableInstance.getDefault().getTable("vision");
        int flags = EntryListenerFlags.kNew | EntryListenerFlags.kUpdate;

        table.getEntry("angleToLine").addListener(event -> {
            angleToLine = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, flags);
        table.getEntry("distanceToLine").addListener(event -> {
            distanceToLine = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, flags);
        table.getEntry("angleToWall").addListener(event -> {
            angleToWall = event.value.getDouble();
            lastUpdate = System.currentTimeMillis();
        }, flags);
    }

    public static boolean isTimedOut() {
        return System.currentTimeMillis() - lastUpdate > TIMEOUT;
    }

}
package frc.robot;

public class Constants {
    public static final int MOTOR1VAL = 34;

    public static final int LFMASTER_ID = 26, LFSLAVE_ID = 24, RFMASTER_ID = 25, RFSLAVE_ID = 23, LBMASTER_ID = 28,
            LBSLAVE_ID = 30, RBMASTER_ID = 27, RBSLAVE_ID = 29;

    public static final int kSlotIdx = 0;

    public static final double SIGMOID_A = 6;
    /**
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For now
     * we just want the primary one.
     */
    public static final int kPIDLoopIdx = 0;

    /**
     * Set to zero to skip waiting for confirmation, set to nonzero to wait and
     * report to DS if action fails.
     */
    public static final int kTimeoutMs = 0;
    public static final double kP = 0.01, kI = 0.000, kD = 10, kF = 1023.0 / 7200.0, iZ = 300, peakOut = 1.0;
                                    //0.25, 0.001, 20            1023.0 / 7200.0     300
}
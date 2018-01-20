package org.usfirst.frc.team1923.robot;

public class RobotMap {

    /**
     * Which PID slot to pull gains from. Starting 2018, you can choose from 0,1,2
     * or 3. Only the first two (0,1) are visible in web-based configuration.
     */
    public static final int kSlotIdx = 0;

    /*
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For now
     * we just want the primary one.
     */
    public static final int kPIDLoopIdx = 0;

    /*
     * set to zero to skip waiting for confirmation, set to nonzero to wait and
     * report to DS if action fails.
     */
    public static final int kTimeoutMs = 10;

    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    public static final int[] LEFT_TALON_PORTS = { 1, 2, 3 };
    public static final int[] RIGHT_TALON_PORTS = { 4, 5, 6 };

    public static final int[] LEFT_DRIVE_PORTS = {};
    public static final int[] RIGHT_DRIVE_PORTS = {};

}
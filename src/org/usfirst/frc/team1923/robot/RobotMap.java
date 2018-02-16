package org.usfirst.frc.team1923.robot;

public class RobotMap {

    // Ports
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    public static final int PCM_MODULE_PORT = 0;

    public static final int[] DRIVE_LEFT_TALON_PORTS = {3, 2, 1};
    public static final int[] DRIVE_RIGHT_TALON_PORTS = {10, 11, 12};

    public static final int PIGEON_IMU_PORT = 10;

    public static final int[] ELEVATOR_TALON_PORTS = {4, 9};

    public static final int INTAKE_LEFT_TALON_PORT = 5;
    public static final int INTAKE_RIGHT_TALON_PORT = 8;

    public static final int INTAKE_LEFT_ULTRASONIC_PING_PORT = 1;
    public static final int INTAKE_LEFT_ULTRASONIC_ECHO_PORT = 2;
    public static final int INTAKE_RIGHT_ULTRASONIC_PING_PORT = 3;
    public static final int INTAKE_RIGHT_ULTRASONIC_ECHO_PORT = 4;

    public static final int INTAKE_SOLENOID_FORWARD_PORT = 3;
    public static final int INTAKE_SOLENOID_REVERSE_PORT = 7;

    // Variables
    public static final int TALON_COMMAND_TIMEOUT = 10;

    public static final String TRAJECTORY_STORE_DIR = "/tmp/trajectories";

}

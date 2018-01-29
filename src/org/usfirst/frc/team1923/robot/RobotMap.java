package org.usfirst.frc.team1923.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class RobotMap {

    /**
     * Ports
     */
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    public static final int PCM_MODULE_PORT = 12;

    public static final int[] DRIVE_LEFT_TALON_PORTS = { 2, 3, 4 };
    public static final int[] DRIVE_RIGHT_TALON_PORTS = { 5, 6, 7 };
  
    public static final int[] ELEVATOR_TALON_PORTS = {1, 10};

    public static final int INTAKE_LEFT_TALON_PORT = 8;
    public static final int INTAKE_RIGHT_TALON_PORT = 9;

    public static final int INTAKE_LEFT_ULTRASONIC_PING_PORT = 10;
    public static final int INTAKE_LEFT_ULTRASONIC_ECHO_PORT = 11;
    public static final int INTAKE_RIGHT_ULTRASONIC_PING_PORT = 10;
    public static final int INTAKE_RIGHT_ULTRASONIC_ECHO_PORT = 11;
    
    public static final int INTAKE_SOLENOID_FORWARD_PORT = 1;
    public static final int INTAKE_SOLENOID_REVERSE_PORT = 2;

    /**
     * Variables
     */
    public static final int TALON_COMMAND_TIMEOUT = 10;

}

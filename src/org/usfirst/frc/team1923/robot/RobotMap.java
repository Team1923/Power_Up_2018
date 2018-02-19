package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.utils.Converter;

/**
 * Class of constants. Unless otherwise specified, all measurements are in inches, angles in degrees, and times in seconds.
 */
public abstract class RobotMap {

    public static abstract class Robot {

        public static final int PCM_PORT = 0;
        public static final int PDP_PORT = 0;

        public static final int CTRE_COMMAND_TIMEOUT_MS = 15; // milliseconds

        public static final int ENCODER_TICKS_PER_ROTATION = 4096; // encoder ticks

        public static final double TALON_NOMINAL_OUTPUT_VOLTAGE = 11.5; // volts

    }

    public static abstract class Drivetrain {

        public static final int[] LEFT_TALON_PORTS = {3, 2, 1};
        public static final int[] RIGHT_TALON_PORTS = {10, 11, 12};

        public static final int PIGEON_IMU_PORT = 10;

        public static final double TRAJECTORY_MAX_VELOCITY = 72; // inches per second
        public static final double TRAJECTORY_MAX_ACCELERATION = 36; // inches per second^2
        public static final double TRAJECTORY_MAX_JERK = 72; // inches per second^3
        public static final double TRAJECTORY_VELOCITY_CONSTANT = 0.265;

        public static final double TRAJECTORY_P = 0.250;
        public static final double TRAJECTORY_I = 0.000;
        public static final double TRAJECTORY_D = 0.000;

        public static final String TRAJECTORY_STORE_DIR = "/tmp/trajectories";

        public static final double P = 0.455;
        public static final double I = 0.001;
        public static final double D = 0.000;
        public static final double F = 0.000;
        public static final int ALLOWABLE_ERROR = 300; // encoder ticks

        public static final double LENGTH = 32.50; // inches
        public static final double WIDTH = 27.50; // inches
        public static final double WHEELBASE_WIDTH = 24.33; // inches
        public static final double WHEEL_DIAMETER = 6; // inches

    }

    public static abstract class Controller {

        public static final int DRIVER_PORT = 0;
        public static final int OPERATOR_PORT = 1;

        public static final double TRIGGER_SENSITIVITY = 0.5;

    }

    public static abstract class Intake {

        public static final int LEFT_TALON_PORT = 5;
        public static final int RIGHT_TALON_PORT = 8;

        public static final int LEFT_ULTRASONIC_PING_PORT = 0;
        public static final int LEFT_ULTRASONIC_ECHO_PORT = 1;
        public static final int RIGHT_ULTRASONIC_PING_PORT = 2;
        public static final int RIGHT_ULTRASONIC_ECHO_PORT = 3;

        public static final int SOLENOID_OPEN_PORT = 3;
        public static final int SOLENOID_CLOSE_PORT = 7;
        public static final int SOLENOID_RAISE_PORT = 1;
        public static final int SOLENOID_LOWER_PORT = 2;

    }

    public static abstract class Elevator {

        public static final int[] TALON_PORTS = {4, 9};

        public static final double MAX_VELOCITY = 36; // inches per second
        public static final double MAX_ACCELERATION = 12; // inches per second^2
        public static final double VELOCITY_CONSTANT = 1 / 60.0;

        public static final double P = 0.455;
        public static final double I = 0.001;
        public static final double D = 0.000;
        public static final double F = 0.000;
        public static final int ALLOWABLE_ERROR = 300; // encoder ticks

        public static final int PULLY_TOOTH_COUNT = 30;
        public static final double BELT_PITCH_MM = 5.0; // millimeters
        public static final double PRIMARY_STAGE_TRAVEL = 50.0; // inches
        public static final double SECONDARY_STAGE_TRAVEL = 45.0; // inches

        // Calculated Value
        public static final double PULLEY_DIAMETER = ((PRIMARY_STAGE_TRAVEL + SECONDARY_STAGE_TRAVEL) * Converter.millimetersToInches(BELT_PITCH_MM) * PULLY_TOOTH_COUNT) / (Math.PI * SECONDARY_STAGE_TRAVEL);

    }

}

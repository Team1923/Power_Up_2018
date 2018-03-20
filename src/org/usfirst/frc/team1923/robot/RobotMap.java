package org.usfirst.frc.team1923.robot;

/**
 * Class of constants. Unless otherwise specified, all measurements are in inches, angles in degrees, and times in seconds.
 */
public abstract class RobotMap {

    public static abstract class Robot {

        /**
         * Ports
         */
        public static final int PCM_PORT = 0;
        public static final int PDP_PORT = 0;

        /**
         * TalonSRX General Configuration
         */
        public static final int CTRE_COMMAND_TIMEOUT_MS = 15; // milliseconds

        public static final int ENCODER_TICKS_PER_ROTATION = 4096; // encoder ticks

        public static final double TALON_NOMINAL_OUTPUT_VOLTAGE = 11.5; // volts

    }

    public static abstract class Controller {

        /**
         * Ports
         */
        public static final int DRIVER_PORT = 0;
        public static final int OPERATOR_PORT = 1;

        /**
         * Controller General Configuration
         */
        public static final double TRIGGER_SENSITIVITY = 0.5;

    }

    public static abstract class Drivetrain {

        /**
         * Ports
         */
        public static final int[] LEFT_TALON_PORTS = {3, 2, 1};
        public static final int[] RIGHT_TALON_PORTS = {10, 11, 12};

        public static final int PIGEON_IMU_PORT = 10;

        /**
         * Configuration for TalonSRX Motion Magic Following
         */
        public static final double MM_MAX_VELOCITY = 72; // inches per second
        public static final double MM_MAX_ACCELERATION = 32; // inches per second^2

        public static final double MM_P = 0.455;
        public static final double MM_I = 0.000;
        public static final double MM_D = 0.000;
        public static final double MM_F = 0.000;
        public static final int MM_ALLOWABLE_ERROR = 500; // encoder ticks

        /**
         * Configuration for Pathfinder Trajectory following
         */
        public static final double TRAJ_MAX_VELOCITY = 72; // inches per second
        public static final double TRAJ_MAX_ACCELERATION = 72; // inches per second^2
        public static final double TRAJ_MAX_JERK = 144; // inches per second^3

        public static final double TRAJ_P = 0.485;
        public static final double TRAJ_I = 0.000;
        public static final double TRAJ_D = 0.000;
        public static final double TRAJ_F = 0.040;

        public static final String TRAJECTORY_STORE_DIR = "/tmp/trajectories";

        /**
         * Physical Dimensions
         */
        public static final double LENGTH = 32.50; // inches
        public static final double WIDTH = 27.50; // inches
        public static final double WHEELBASE_WIDTH = 24.33; // inches
        public static final double WHEEL_DIAMETER = 6; // inches

        /**
         * TalonSRX General Configuration
         */
        public static final double TALON_RAMP_RATE = 0.125; // seconds

        public static final int TALON_STATUS_FRAME_PERIOD_MS = 10; // milliseconds
        public static final int TALON_CONTROL_FRAME_PERIOD_MS = 5; // milliseconds

    }

    public static abstract class Intake {

        /**
         * Ports
         */
        public static final int LEFT_TALON_PORT = 5;
        public static final int RIGHT_TALON_PORT = 8;

        public static final int LEFT_ULTRASONIC_PING_PORT = 0;
        public static final int LEFT_ULTRASONIC_ECHO_PORT = 1;
        public static final int RIGHT_ULTRASONIC_PING_PORT = 2;
        public static final int RIGHT_ULTRASONIC_ECHO_PORT = 3;

        public static final int SOLENOID_OPEN_PORT = 3;
        public static final int SOLENOID_CLOSE_PORT = 4;
        public static final int SOLENOID_RAISE_PORT = 5;
        public static final int SOLENOID_LOWER_PORT = 2;

    }

    public static abstract class Elevator {

        /**
         * Ports
         */
        public static final int[] TALON_PORTS = {9, 4};

        /**
         * Configuration for TalonSRX Motion Magic Following
         */
        public static final double MM_MAX_VELOCITY = 50; // inches per second
        public static final double MM_MAX_ACCELERATION = 50; // inches per second^2

        public static final double MM_P = 0.455;
        public static final double MM_I = 0.000;
        public static final double MM_D = 0.000;
        public static final double MM_F = 0.120;
        public static final int MM_ALLOWABLE_ERROR = 300; // encoder ticks

        /**
         * Physical Dimensions
         */
        public static final int PULLY_TOOTH_COUNT = 30;
        public static final double BELT_PITCH_MM = 5.0; // millimeters
        public static final double PRIMARY_STAGE_TRAVEL = 74; // inches
        public static final double SECONDARY_STAGE_TRAVEL = 0; // inches

        // Calculated Value
        //public static final double PULLEY_DIAMETER = ((PRIMARY_STAGE_TRAVEL + SECONDARY_STAGE_TRAVEL) * Converter.millimetersToInches(BELT_PITCH_MM) * PULLY_TOOTH_COUNT) / (Math.PI * SECONDARY_STAGE_TRAVEL);

        public static final double PULLEY_DIAMETER = 3.735095379620145; // TODO: Recalculate accurately

        // carr: 46 in
        // slide: 39.5
    }

}

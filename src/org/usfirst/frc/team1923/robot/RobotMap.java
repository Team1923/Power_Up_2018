package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.utils.PIDF;

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
        public static final int PIDGEON_UNITS_PER_ROTATION = 8192; // pidgeon imu native units

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

        public static final int PIGEON_IMU_PORT = 12;

        /**
         * Configuration for TalonSRX Gyro (Pigeon IMU)
         */
        public static final PIDF GYRO_PIDF = new PIDF(3.5, 0.0, 8.0, 0.0);

        /**
         * Configuration for TalonSRX Motion Magic Following
         */
        public static final double MM_MAX_VELOCITY = 72; // inches per second
        public static final double MM_MAX_ACCELERATION = 32; // inches per second^2

        public static final PIDF MM_PIDF = new PIDF(0.185, 0.0, 0.0, 0.35);

        public static final int MM_ALLOWABLE_ERROR = 200; // encoder ticks

        public static final int MMT_MAX_VELOCITY = 10; // degrees per second
        public static final int MMT_MAX_ACCELERATION = 10; // degrees per second
        public static final int MMT_ALLOWABLE_ERROR = 3; // degrees

        /**
         * Configuration for Pathfinder Trajectory following
         */
        public static final double TRAJ_MAX_VELOCITY = 84; // inches per second
        public static final double TRAJ_MAX_ACCELERATION = 84; // inches per second^2
        public static final double TRAJ_MAX_JERK = 144; // inches per second^3

        public static final PIDF TRAJ_PIDF = new PIDF(0.225, 0.0, 0.0, 0.35); // TODO: Old F = 0.04 when using ticks / sec for velocity
        // TODO: Old: 0.485, 0.0, 0.0, 0.25

        public static final String TRAJECTORY_STORE_DIR = "/home/lvuser/trajectories";

        /**
         * Physical Dimensions
         */
        public static final double WHEELBASE_WIDTH = 48.5; // inches, empirically calculated
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
        public static final double MM_MAX_VELOCITY = 200; // inches per second
        public static final double MM_MAX_ACCELERATION = 180; // inches per second^2

        public static final PIDF MM_PIDF = new PIDF(0.455, 0.0, 0.0, 0.12);
        public static final int MM_ALLOWABLE_ERROR = 800; // encoder ticks

        /**
         * Physical Dimensions
         */
        public static final int PULLY_TOOTH_COUNT = 30;
        public static final double BELT_PITCH_MM = 5.0; // millimeters
        public static final double PRIMARY_STAGE_TRAVEL = 74; // inches
        public static final double SECONDARY_STAGE_TRAVEL = 0; // inches

        // Calculated Valueinal double PULLEY_DIAMETER = ((PRIMARY_STAGE_TRAVEL + SECONDARY_STAGE_TRAVEL) * Converter.millimetersToInches(BELT_PITCH_MM) * PULLY_TOOTH_COUNT) / (Math.PI * SECONDARY_STAGE_TRAVEL);

        //public static f
        public static final double PULLEY_DIAMETER = 3.735095379620145; // TODO: Recalculate accurately

        // carr: 46 in
        // slide: 39.5
    }

}

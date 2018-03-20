package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.DriveControlCommand;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.EncoderCalculator;

public class DrivetrainSubsystem extends Subsystem {

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    private PigeonIMU imu;

    private EncoderCalculator leftEncoder;
    private EncoderCalculator rightEncoder;

    public DrivetrainSubsystem() {
        this.leftTalons = new TalonSRX[RobotMap.Drivetrain.LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.Drivetrain.RIGHT_TALON_PORTS.length];

        for (int i = 0; i < this.leftTalons.length; i++) {
            this.leftTalons[i] = new TalonSRX(RobotMap.Drivetrain.LEFT_TALON_PORTS[i]);

            if (i > 0) {
                this.leftTalons[i].set(ControlMode.Follower, RobotMap.Drivetrain.LEFT_TALON_PORTS[0]);
            }

            this.leftTalons[i].setInverted(true);
            this.configureTalon(this.leftTalons[i]);
        }

        for (int i = 0; i < this.rightTalons.length; i++) {
            this.rightTalons[i] = new TalonSRX(RobotMap.Drivetrain.RIGHT_TALON_PORTS[i]);

            if (i > 0) {
                this.rightTalons[i].set(ControlMode.Follower, RobotMap.Drivetrain.RIGHT_TALON_PORTS[0]);
            }

            this.configureTalon(this.rightTalons[i]);
        }

        this.imu = new PigeonIMU(RobotMap.Drivetrain.PIGEON_IMU_PORT);

        this.leftEncoder = new EncoderCalculator(RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.rightEncoder = new EncoderCalculator(RobotMap.Drivetrain.WHEEL_DIAMETER);

        Notifier notifier = new Notifier(() -> {
            this.leftTalons[0].processMotionProfileBuffer();
            this.rightTalons[0].processMotionProfileBuffer();
        });
        notifier.startPeriodic(0.005);

//        Robot.logger.addDataSource("Drivetrain_LeftEncoder", () -> this.getLeftEncoderPosition() + "");
//        Robot.logger.addDataSource("Drivetrain_RightEncoder", () -> this.getRightEncoderPosition() + "");
//        Robot.logger.addDataSource("Drivetrain_LeftVelocity", () -> String.format("%.4g%n", this.leftEncoder.getVelocity()));
//        Robot.logger.addDataSource("Drivetrain_RightVelocity", () -> String.format("%.4g%n", this.rightEncoder.getVelocity()));
//        Robot.logger.addDataSource("Drivetrain_LeftAccel", () -> String.format("%.4g%n", this.leftEncoder.getAcceleration()));
//        Robot.logger.addDataSource("Drivetrain_RightAccel", () -> String.format("%.4g%n", this.rightEncoder.getAcceleration()));
//        Robot.logger.addDataSource("Drivetrain_LeftTalonMode", () -> this.leftTalons[0].getControlMode().name());
//        Robot.logger.addDataSource("Drivetrain_RightTalonMode", () -> this.rightTalons[0].getControlMode().name());
    }

    public void drive(double leftOutput, double rightOutput) {
        this.drive(ControlMode.PercentOutput, leftOutput, rightOutput);
    }

    public void drive(ControlMode controlMode, double leftOutput, double rightOutput) {
        this.leftTalons[0].set(controlMode, leftOutput);
        this.rightTalons[0].set(controlMode, rightOutput);
    }

    public void setMotionProfile(SetValueMotionProfile setValueMotionProfile) {
        this.leftTalons[0].set(ControlMode.MotionProfile, setValueMotionProfile.value);
        this.rightTalons[0].set(ControlMode.MotionProfile, setValueMotionProfile.value);
    }

    public void resetPosition() {
        this.leftTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.leftTalons[0].setSelectedSensorPosition(0, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].setSelectedSensorPosition(0, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public int getLeftEncoderPosition() {
        return this.leftTalons[0].getSelectedSensorPosition(0);
    }

    public int getRightEncoderPosition() {
        return this.rightTalons[0].getSelectedSensorPosition(0);
    }

    public void stop() {
        this.drive(ControlMode.PercentOutput, 0, 0);
    }

    public double getHeading() {
        return this.imu.getFusedHeading();
    }

    public void resetHeading() {
        this.imu.setFusedHeading(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    @Override
    public void periodic() {
        this.leftEncoder.calculate(this.leftTalons[0].getSelectedSensorVelocity(0));
        this.rightEncoder.calculate(this.leftTalons[0].getSelectedSensorVelocity(0));

        SmartDashboard.putNumber("Left DT Velocity", Converter.inchesToFeet(this.leftEncoder.getVelocity()));
        SmartDashboard.putNumber("Left DT Accel.", Converter.inchesToFeet(this.leftEncoder.getAcceleration()));

        SmartDashboard.putNumber("Right DT Velocity", Converter.inchesToFeet(this.rightEncoder.getVelocity()));
        SmartDashboard.putNumber("Right DT Accel.", Converter.inchesToFeet(this.rightEncoder.getAcceleration()));

        SmartDashboard.putNumber("Left Encoder", this.getLeftEncoderPosition());
        SmartDashboard.putNumber("Right Encoder", this.getRightEncoderPosition());
    }

    public TalonSRX getLeftMasterTalon() {
        return this.leftTalons[0];
    }

    public TalonSRX getRightMasterTalon() {
        return this.rightTalons[0];
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new DriveControlCommand());
    }
 
    public static double distanceToRotations(double distance) {
        return distance / (RobotMap.Drivetrain.WHEEL_DIAMETER * Math.PI);
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configNominalOutputReverse(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputForward(1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputReverse(-1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.configMotionAcceleration(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_ACCELERATION, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configMotionProfileTrajectoryPeriod(20, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(0, RobotMap.Drivetrain.MM_P, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(0, RobotMap.Drivetrain.MM_I, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(0, RobotMap.Drivetrain.MM_D, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(0, RobotMap.Drivetrain.MM_F, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(1, RobotMap.Drivetrain.TRAJ_P, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(1, RobotMap.Drivetrain.TRAJ_I, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(1, RobotMap.Drivetrain.TRAJ_D, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(1, RobotMap.Drivetrain.TRAJ_F, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configOpenloopRamp(RobotMap.Drivetrain.TALON_RAMP_RATE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configClosedloopRamp(RobotMap.Drivetrain.TALON_RAMP_RATE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setControlFramePeriod(ControlFrame.Control_3_General, RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);
        talon.changeMotionControlFramePeriod(RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);
    }

}

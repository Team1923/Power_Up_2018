package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.DriveControlCommand;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.EncoderCalculator;
import org.usfirst.frc.team1923.robot.utils.PIDF;

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

        this.imu = new PigeonIMU(this.rightTalons[2]);

        this.configureMasterTalon(this.leftTalons[0]);
        this.configureMasterTalon(this.rightTalons[0]);

        // Aux PID Polarity = False : Output = PID[0] + PID[1]
        // Aux PID Polarity = True : Output = PID[0] - PID[1]
        this.leftTalons[0].configAuxPIDPolarity(true, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].configAuxPIDPolarity(false, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.leftEncoder = new EncoderCalculator(RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.rightEncoder = new EncoderCalculator(RobotMap.Drivetrain.WHEEL_DIAMETER);

        Notifier notifier = new Notifier(() -> {
            this.leftTalons[0].processMotionProfileBuffer();
            this.rightTalons[0].processMotionProfileBuffer();
        });
        notifier.startPeriodic(0.005);
    }

    public void drive(double leftOutput, double rightOutput) {
        this.drive(ControlMode.PercentOutput, leftOutput, rightOutput);
    }

    public void drive(ControlMode controlMode, double leftOutput, double rightOutput) {
        this.leftTalons[0].set(controlMode, leftOutput);
        this.rightTalons[0].set(controlMode, rightOutput);
    }

    public void setMotionProfile(SetValueMotionProfile setValueMotionProfile) {
        this.leftTalons[0].set(ControlMode.MotionProfileArc, setValueMotionProfile.value);
        this.rightTalons[0].set(ControlMode.MotionProfileArc, setValueMotionProfile.value);
    }

    public void resetPosition() {
        this.leftTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.leftTalons[0].setSelectedSensorPosition(0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].setSelectedSensorPosition(0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public int getLeftEncoderPosition() {
        return this.leftTalons[0].getSelectedSensorPosition(PIDF.PRIMARY_LOOP);
    }

    public int getRightEncoderPosition() {
        return this.rightTalons[0].getSelectedSensorPosition(PIDF.PRIMARY_LOOP);
    }

    public void stop() {
        this.drive(ControlMode.PercentOutput, 0, 0);
    }

    public double getHeading() {
        return this.imu.getFusedHeading();
    }

    public void resetHeading() {
        this.imu.setFusedHeading(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.imu.setYaw(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    @Override
    public void periodic() {
        this.leftEncoder.calculate(this.leftTalons[0].getSelectedSensorVelocity(PIDF.PRIMARY_LOOP));
        this.rightEncoder.calculate(this.leftTalons[0].getSelectedSensorVelocity(PIDF.PRIMARY_LOOP));
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

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configNominalOutputReverse(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputForward(1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputReverse(-1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Drivetrain.MM_PIDF.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Drivetrain.MM_PIDF.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Drivetrain.MM_PIDF.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Drivetrain.MM_PIDF.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(PIDF.TALON_GYRO_SLOT, RobotMap.Drivetrain.GYRO_PIDF.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(PIDF.TALON_GYRO_SLOT, RobotMap.Drivetrain.GYRO_PIDF.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(PIDF.TALON_GYRO_SLOT, RobotMap.Drivetrain.GYRO_PIDF.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(PIDF.TALON_GYRO_SLOT, RobotMap.Drivetrain.GYRO_PIDF.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configClosedLoopPeakOutput(PIDF.TALON_GYRO_SLOT, 0.5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(PIDF.TALON_GYROTURN_SLOT, RobotMap.Drivetrain.MMT_PIDF.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(PIDF.TALON_GYROTURN_SLOT, RobotMap.Drivetrain.MMT_PIDF.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(PIDF.TALON_GYROTURN_SLOT, RobotMap.Drivetrain.MMT_PIDF.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(PIDF.TALON_GYROTURN_SLOT, RobotMap.Drivetrain.MMT_PIDF.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(PIDF.TALON_MOTIONPROFILE_SLOT, RobotMap.Drivetrain.TRAJ_PIDF.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(PIDF.TALON_MOTIONPROFILE_SLOT, RobotMap.Drivetrain.TRAJ_PIDF.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(PIDF.TALON_MOTIONPROFILE_SLOT, RobotMap.Drivetrain.TRAJ_PIDF.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(PIDF.TALON_MOTIONPROFILE_SLOT, RobotMap.Drivetrain.TRAJ_PIDF.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configOpenloopRamp(RobotMap.Drivetrain.TALON_RAMP_RATE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        // talon.configClosedloopRamp(RobotMap.Drivetrain.TALON_RAMP_RATE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    private void configureMasterTalon(TalonSRX talon) {
        talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.setControlFramePeriod(ControlFrame.Control_3_General, RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);
        talon.changeMotionControlFramePeriod(RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);

        talon.configRemoteFeedbackFilter(RobotMap.Drivetrain.PIGEON_IMU_PORT, RemoteSensorSource.GadgeteerPigeon_Yaw, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configSelectedFeedbackSensor(RemoteFeedbackDevice.RemoteSensor0, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configSelectedFeedbackCoefficient(1, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configSelectedFeedbackCoefficient(3600.0 / RobotMap.Robot.PIDGEON_UNITS_PER_ROTATION, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.configMotionAcceleration(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_ACCELERATION, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configMotionProfileTrajectoryPeriod(20, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.selectProfileSlot(PIDF.TALON_MOTIONMAGIC_SLOT, PIDF.PRIMARY_LOOP);
        talon.selectProfileSlot(PIDF.TALON_GYRO_SLOT, PIDF.AUXILIARY_LOOP);

        talon.setSensorPhase(false);
    }

    /**
     * Configure master talons for turning
     */
    public void configTalonTurning() {
        this.leftTalons[0].configSelectedFeedbackSensor(RemoteFeedbackDevice.RemoteSensor0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.leftTalons[0].configMotionCruiseVelocity(Converter.degreesToPidgeonTicks(RobotMap.Drivetrain.MMT_MAX_VELOCITY), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.leftTalons[0].configMotionCruiseVelocity(Converter.degreesToPidgeonTicks(RobotMap.Drivetrain.MMT_MAX_ACCELERATION), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.leftTalons[0].selectProfileSlot(PIDF.TALON_GYROTURN_SLOT, PIDF.PRIMARY_LOOP);
        this.leftTalons[0].setSensorPhase(true);

        this.rightTalons[0].configSelectedFeedbackSensor(RemoteFeedbackDevice.RemoteSensor0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].configMotionCruiseVelocity(Converter.degreesToPidgeonTicks(RobotMap.Drivetrain.MMT_MAX_VELOCITY), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].configMotionCruiseVelocity(Converter.degreesToPidgeonTicks(RobotMap.Drivetrain.MMT_MAX_ACCELERATION), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].selectProfileSlot(PIDF.TALON_GYROTURN_SLOT, PIDF.PRIMARY_LOOP);
    }

    /**
     * Configure master talons for driving
     */
    public void configTalonDriving() {
        this.configureMasterTalon(this.leftTalons[0]);
        this.configureMasterTalon(this.rightTalons[0]);
    }

}

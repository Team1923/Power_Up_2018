package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.DriveControlCommand;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

public class DrivetrainSubsystem extends Subsystem {

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    private PigeonIMU pigeon;

    public DrivetrainSubsystem() {
        this.leftTalons = this.initializeTalons(RobotMap.Drivetrain.LEFT_TALON_PORTS);
        this.rightTalons = this.initializeTalons(RobotMap.Drivetrain.RIGHT_TALON_PORTS);

        this.configureTalons();

        this.pigeon = new PigeonIMU(this.rightTalons[2]); // TODO: Use PigeonIMU port in RobotMap
        this.pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.BiasedStatus_2_Gyro,5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_1_General, 5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_3_GeneralAccel, 5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        Notifier notifier = new Notifier(() -> this.getRightMaster().processMotionProfileBuffer());
        notifier.startPeriodic(0.005);
    }

    public void drive(double leftOutput, double rightOutput) {
        this.drive(ControlMode.PercentOutput, leftOutput, rightOutput);
    }

    public void drive(ControlMode controlMode, double leftOutput, double rightOutput) {
        this.getLeftMaster().set(controlMode, leftOutput);
        this.getRightMaster().set(controlMode, rightOutput);
    }

    public void stop() {
        this.drive(0, 0);

        this.getLeftMaster().neutralOutput();
        this.getRightMaster().neutralOutput();
    }

    /**
     * Configure talons for normal driving use (or MotionMagic)
     */
    public void configureDriving() {
        this.stop(); // Clears any follower cases on the master talons
        this.configureTalons();

        this.getLeftMaster().follow(this.getRightMaster(), FollowerType.AuxOutput1);
    }

    /**
     * Configure talons for MotionMagic turning with PigeonIMU
     */
    public void configureTurning() {
        for (TalonSRX talon : this.leftTalons) {
            talon.setInverted(false);
        }

        this.getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.SensorSum, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackCoefficient(3600.0 / RobotMap.Robot.PIDGEON_UNITS_PER_ROTATION, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackCoefficient(1.0, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.getRightMaster().configMotionAcceleration(Converter.degreesToPidgeonTicks(RobotMap.Drivetrain.MMT_MAX_ACCELERATION) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configMotionCruiseVelocity(Converter.degreesToPidgeonTicks(RobotMap.Drivetrain.MMT_MAX_VELOCITY) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.getRightMaster().selectProfileSlot(PIDF.TALON_GYRO_SLOT, PIDF.PRIMARY_LOOP);
        this.getRightMaster().selectProfileSlot(PIDF.TALON_MOTIONMAGIC_SLOT, PIDF.AUXILIARY_LOOP);

        this.getLeftMaster().follow(this.getRightMaster(), FollowerType.AuxOutput1);
    }

    public void resetPosition() {
        this.getLeftMaster().getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.getLeftMaster().setSelectedSensorPosition(0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().setSelectedSensorPosition(0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public void resetHeading() {
        this.pigeon.setFusedHeading(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.pigeon.setYaw(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        // this.getRightMaster().setSelectedSensorPosition(0, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public TalonSRX getLeftMaster() {
        return this.leftTalons[0];
    }

    public TalonSRX getRightMaster() {
        return this.rightTalons[0];
    }

    public double getHeading() {
        double[] ypr = new double[3];
        this.pigeon.getYawPitchRoll(ypr);

        return ypr[0];
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new DriveControlCommand());
    }

    private TalonSRX[] initializeTalons(int[] talonPorts) {
        TalonSRX[] talons = new TalonSRX[talonPorts.length];

        for (int i = 0; i < talonPorts.length; i++) {
            talons[i] = new TalonSRX(talonPorts[i]);

            if (i > 0) {
                talons[i].set(ControlMode.Follower, talonPorts[0]);
            }

            talons[i].configNominalOutputForward(0.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talons[i].configNominalOutputReverse(0.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talons[i].configPeakOutputForward(1.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talons[i].configPeakOutputReverse(-1.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

            talons[i].enableVoltageCompensation(true);
            talons[i].configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talons[i].configOpenloopRamp(RobotMap.Drivetrain.TALON_RAMP_RATE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        }

        return talons;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Aux PID", this.getRightMaster().getSelectedSensorPosition(PIDF.AUXILIARY_LOOP));
    }

    private void configureTalons() {
        // Invert Talons
        for (TalonSRX talon : this.leftTalons) {
            talon.setInverted(true);
        }

        // Control/Status Frames Configuration
        this.getRightMaster().setStatusFramePeriod(StatusFrame.Status_2_Feedback0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().setStatusFramePeriod(StatusFrame.Status_12_Feedback1, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().setControlFramePeriod(ControlFrame.Control_3_General, RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);
        this.getRightMaster().changeMotionControlFramePeriod(RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);

        this.getLeftMaster().setStatusFramePeriod(StatusFrame.Status_2_Feedback0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        // Sensor Configuration
        this.getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getLeftMaster().configSelectedFeedbackSensor(FeedbackDevice.None, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getLeftMaster().configSelectedFeedbackCoefficient(1.0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getLeftMaster().configSelectedFeedbackCoefficient(1.0, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.getRightMaster().configRemoteFeedbackFilter(this.getLeftMaster().getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configRemoteFeedbackFilter(RobotMap.Drivetrain.PIGEON_IMU_PORT, RemoteSensorSource.GadgeteerPigeon_Yaw, 1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.SensorSum, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackCoefficient(1.0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configSelectedFeedbackCoefficient(3600.0 / RobotMap.Robot.PIDGEON_UNITS_PER_ROTATION, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        // PIDF Configuration
        this.configurePIDF(this.getRightMaster(), PIDF.TALON_MOTIONPROFILE_SLOT, RobotMap.Drivetrain.TRAJ_PIDF);
        this.configurePIDF(this.getRightMaster(), PIDF.TALON_GYRO_SLOT, RobotMap.Drivetrain.GYRO_PIDF);
        this.configurePIDF(this.getRightMaster(), PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Drivetrain.MM_PIDF);
        this.getRightMaster().configClosedLoopPeakOutput(PIDF.TALON_GYRO_SLOT, 0.35, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.getRightMaster().selectProfileSlot(PIDF.TALON_MOTIONMAGIC_SLOT, PIDF.PRIMARY_LOOP);
        this.getRightMaster().selectProfileSlot(PIDF.TALON_GYRO_SLOT, PIDF.AUXILIARY_LOOP);

        // Motion Profiling Configuration
        this.getRightMaster().configMotionAcceleration(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_ACCELERATION, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configMotionProfileTrajectoryPeriod(20, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    private void configurePIDF(TalonSRX talon, int slotId, PIDF pidf) {
        talon.config_kP(slotId, pidf.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(slotId, pidf.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(slotId, pidf.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(slotId, pidf.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

}

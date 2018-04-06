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

import java.util.Arrays;

public class DrivetrainSubsystem extends Subsystem {

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    private PigeonIMU pigeon;

    public DrivetrainSubsystem() {
        this.leftTalons = this.initializeTalons(RobotMap.Drivetrain.LEFT_TALON_PORTS);
        this.rightTalons = this.initializeTalons(RobotMap.Drivetrain.RIGHT_TALON_PORTS);

        this.configureTalons();
        this.configureDriving();

        for (TalonSRX talon : this.getTalons()) {
            if (talon.getDeviceID() == RobotMap.Drivetrain.PIGEON_IMU_PORT) {
                this.pigeon = new PigeonIMU(talon);
            }
        }

        this.pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
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
        for (TalonSRX talon : this.leftTalons) {
            talon.setInverted(true);
        }

        for (TalonSRX talon : this.getTalons()) {
            int deviceId = talon.getDeviceID();

            if (deviceId == RobotMap.Drivetrain.LEFT_ENCODER_PORT || deviceId == RobotMap.Drivetrain.RIGHT_ENCODER_PORT) {
                talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configSelectedFeedbackCoefficient(1.0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.setSensorPhase(true);

                talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            }

            if (deviceId == RobotMap.Drivetrain.SUM_ENCODER_PORT) {
                talon.configRemoteFeedbackFilter(RobotMap.Drivetrain.LEFT_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configRemoteFeedbackFilter(RobotMap.Drivetrain.RIGHT_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

                talon.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.RemoteSensor1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

                talon.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configSelectedFeedbackCoefficient(1.0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

                talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, RobotMap.Drivetrain.TALON_STATUS_FRAME_PERIOD_MS, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            }

            if (deviceId == RobotMap.Drivetrain.LEFT_TALON_PORTS[0] || deviceId == RobotMap.Drivetrain.RIGHT_TALON_PORTS[0]) {
                talon.configRemoteFeedbackFilter(RobotMap.Drivetrain.SUM_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configRemoteFeedbackFilter(RobotMap.Drivetrain.PIGEON_IMU_PORT, RemoteSensorSource.GadgeteerPigeon_Yaw, 1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

                talon.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configSelectedFeedbackCoefficient(1.0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configSelectedFeedbackCoefficient(3600.0 / RobotMap.Robot.PIDGEON_UNITS_PER_ROTATION, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

                talon.selectProfileSlot(PIDF.TALON_MOTIONMAGIC_SLOT, PIDF.PRIMARY_LOOP);
                talon.selectProfileSlot(PIDF.TALON_GYRO_SLOT, PIDF.AUXILIARY_LOOP);

                talon.configMotionAcceleration(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_ACCELERATION, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.configMotionProfileTrajectoryPeriod(20, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            }

            if (deviceId == RobotMap.Drivetrain.RIGHT_TALON_PORTS[0]) {
                talon.setSensorPhase(true);
                talon.configAuxPIDPolarity(false, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            }

            if (deviceId == RobotMap.Drivetrain.LEFT_TALON_PORTS[0]) {
                talon.setSensorPhase(false);
                talon.configAuxPIDPolarity(true, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            }
        }
    }

    public void configureSensorSum() {
        this.getLeftMaster().configRemoteFeedbackFilter(RobotMap.Drivetrain.SUM_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configRemoteFeedbackFilter(RobotMap.Drivetrain.SUM_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public void configureIndividualSensor() {
        this.getLeftMaster().configRemoteFeedbackFilter(RobotMap.Drivetrain.LEFT_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.getRightMaster().configRemoteFeedbackFilter(RobotMap.Drivetrain.RIGHT_ENCODER_PORT, RemoteSensorSource.TalonSRX_SelectedSensor, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public void resetPosition() {
        for (TalonSRX talon : this.getTalons()) {
            int deviceId = talon.getDeviceID();

            if (deviceId == RobotMap.Drivetrain.LEFT_ENCODER_PORT || deviceId == RobotMap.Drivetrain.RIGHT_ENCODER_PORT) {
                talon.getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
                talon.setSelectedSensorPosition(0, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            }
        }
    }

    public void resetHeading() {
        this.pigeon.setFusedHeading(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.pigeon.setYaw(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        // this.getRightMaster().setSelectedSensorPosition(0, PIDF.AUXILIARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public TalonSRX getLeftMaster() {
        return this.leftTalons[0];
    }

    public TalonSRX[] getLeftTalons() {
        return this.leftTalons;
    }

    public TalonSRX getRightMaster() {
        return this.rightTalons[0];
    }

    public TalonSRX[] getRightTalons() {
        return this.rightTalons;
    }

    public TalonSRX[] getMasterTalons() {
        return new TalonSRX[]{
                this.getLeftMaster(),
                this.getRightMaster()
        };
    }

    public TalonSRX[] getTalons() {
        TalonSRX[] talons = Arrays.copyOf(this.leftTalons, this.leftTalons.length + this.rightTalons.length);
        System.arraycopy(this.rightTalons, 0, talons, this.leftTalons.length, this.rightTalons.length);

        return talons;
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
        }

        return talons;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Sensor Sum", this.getLeftMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP));
        SmartDashboard.putNumber("RSensor Sum", this.getRightMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP));
        SmartDashboard.putNumber("Heading", this.getLeftMaster().getSelectedSensorPosition(PIDF.AUXILIARY_LOOP));
        SmartDashboard.putNumber("RHeading", this.getRightMaster().getSelectedSensorPosition(PIDF.AUXILIARY_LOOP));
    }

    private void configureTalons() {
        for (TalonSRX talon : this.getTalons()) {
            talon.configNominalOutputForward(0.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talon.configNominalOutputReverse(0.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talon.configPeakOutputForward(1.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talon.configPeakOutputReverse(-1.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

            talon.enableVoltageCompensation(true);
            talon.configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            talon.configOpenloopRamp(RobotMap.Drivetrain.TALON_RAMP_RATE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        }

        for (TalonSRX talon : this.getMasterTalons()) {
            talon.changeMotionControlFramePeriod(RobotMap.Drivetrain.TALON_CONTROL_FRAME_PERIOD_MS);

            this.configurePIDF(talon, PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Drivetrain.MM_PIDF);
            this.configurePIDF(talon, PIDF.TALON_GYRO_SLOT, RobotMap.Drivetrain.GYRO_PIDF);
            this.configurePIDF(talon, PIDF.TALON_MOTIONPROFILE_SLOT, RobotMap.Drivetrain.TRAJ_PIDF);
            this.configurePIDF(talon, PIDF.TALON_TURN_SLOT, RobotMap.Drivetrain.TURN_PIDF);
        }
    }

    private void configurePIDF(TalonSRX talon, int slotId, PIDF pidf) {
        talon.config_kP(slotId, pidf.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(slotId, pidf.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(slotId, pidf.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(slotId, pidf.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configClosedLoopPeakOutput(slotId, 1.0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

}

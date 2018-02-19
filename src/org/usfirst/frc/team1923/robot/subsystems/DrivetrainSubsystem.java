package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        this.leftEncoder = new EncoderCalculator();
        this.rightEncoder = new EncoderCalculator();
    }

    public void drive(double leftOutput, double rightOutput) {
        this.drive(ControlMode.PercentOutput, leftOutput, rightOutput);
    }

    public void drive(ControlMode controlMode, double leftOutput, double rightOutput) {
        this.leftTalons[0].set(controlMode, leftOutput);
        this.rightTalons[0].set(controlMode, rightOutput);
    }

    public void resetPosition() {
        this.leftTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.leftTalons[0].setSelectedSensorPosition(0, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.rightTalons[0].setSelectedSensorPosition(0, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    public int getLeftEncoderPosition() {
        return -this.leftTalons[0].getSensorCollection().getQuadraturePosition();
    }

    public int getRightEncoderPosition() {
        return this.rightTalons[0].getSensorCollection().getQuadraturePosition();
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
        this.leftEncoder.calculate(this.getLeftEncoderPosition());
        this.rightEncoder.calculate(this.getRightEncoderPosition());

        SmartDashboard.putNumber("Left DT Velocity", this.leftEncoder.getVelocity());
        SmartDashboard.putNumber("Left DT Accel.", this.leftEncoder.getAcceleration());

        SmartDashboard.putNumber("Right DT Velocity", this.rightEncoder.getVelocity());
        SmartDashboard.putNumber("Right DT Accel.", this.rightEncoder.getAcceleration());

        SmartDashboard.putNumber("Left Encoder", this.getLeftEncoderPosition());
        SmartDashboard.putNumber("Right Encoder", this.getRightEncoderPosition());
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

        talon.configMotionAcceleration(Converter.inchesToTicks(RobotMap.Drivetrain.TRAJECTORY_MAX_ACCELERATION, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Drivetrain.TRAJECTORY_MAX_VELOCITY, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.config_kP(0, RobotMap.Drivetrain.P, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kI(0, RobotMap.Drivetrain.I, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kD(0, RobotMap.Drivetrain.D, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.config_kF(0, RobotMap.Drivetrain.F, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

}

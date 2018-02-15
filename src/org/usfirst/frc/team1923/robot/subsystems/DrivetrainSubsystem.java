package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1923.robot.Measurement;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.DriveControlCommand;
import org.usfirst.frc.team1923.robot.utils.EncoderCalculator;

public class DrivetrainSubsystem extends Subsystem {

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    private PigeonIMU imu;

    private EncoderCalculator leftEncoder;
    private EncoderCalculator rightEncoder;

    public DrivetrainSubsystem() {
        this.leftTalons = new TalonSRX[RobotMap.DRIVE_LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.DRIVE_RIGHT_TALON_PORTS.length];

        for (int i = 0; i < this.leftTalons.length; i++) {
            this.leftTalons[i] = new TalonSRX(RobotMap.DRIVE_LEFT_TALON_PORTS[i]);

            if (i > 0) {
                this.leftTalons[i].set(ControlMode.Follower, RobotMap.DRIVE_LEFT_TALON_PORTS[0]);
            }

            this.leftTalons[i].setSensorPhase(true);
            this.leftTalons[i].setInverted(true);
            this.configureTalon(this.leftTalons[i]);
        }

        for (int i = 0; i < this.rightTalons.length; i++) {
            this.rightTalons[i] = new TalonSRX(RobotMap.DRIVE_RIGHT_TALON_PORTS[i]);

            if (i > 0) {
                this.rightTalons[i].set(ControlMode.Follower, RobotMap.DRIVE_RIGHT_TALON_PORTS[0]);
            }

            this.configureTalon(this.rightTalons[i]);
        }

        this.imu = new PigeonIMU(RobotMap.PIGEON_IMU_PORT);

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
        this.leftTalons[0].getSensorCollection().setPulseWidthPosition(0, RobotMap.TALON_COMMAND_TIMEOUT);
        this.rightTalons[0].getSensorCollection().setQuadraturePosition(0, RobotMap.TALON_COMMAND_TIMEOUT);
    }

    public int getLeftEncoderPosition() {
        return this.leftTalons[0].getSensorCollection().getQuadraturePosition();
    }

    public int getRightEncoderPosition() {
        return this.rightTalons[0].getSensorCollection().getQuadraturePosition();
    }

    public void stop() {
        this.drive(0, 0);
    }

    public double getHeading() {
        return this.imu.getFusedHeading();
    }

    public void resetHeading() {
        this.imu.setFusedHeading(0, 10);
    }

    @Override
    public void periodic() {
        this.leftEncoder.calculate(this.getLeftEncoderPosition());
        this.rightEncoder.calculate(this.getRightEncoderPosition());

        SmartDashboard.putNumber("Left DT Velocity", this.leftEncoder.getVelocity());
        SmartDashboard.putNumber("Left DT Accel.", this.leftEncoder.getAcceleration());

        SmartDashboard.putNumber("Right DT Velocity", this.rightEncoder.getVelocity());
        SmartDashboard.putNumber("Right DT Accel.", this.rightEncoder.getAcceleration());

        SmartDashboard.putNumber("Left Encoder", Robot.drivetrainSubsystem.getLeftEncoderPosition());
        SmartDashboard.putNumber("Right Encoder", Robot.drivetrainSubsystem.getRightEncoderPosition());
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new DriveControlCommand());
    }
 
    public static double distanceToRotations(double distance) {
        return distance / (Measurement.ROBOT_WHEEL_DIAMETER.inInches() * Math.PI);
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configNominalOutputReverse(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputForward(1, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputReverse(-1, RobotMap.TALON_COMMAND_TIMEOUT);

        talon.configMotionAcceleration(800, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configMotionCruiseVelocity(1600, RobotMap.TALON_COMMAND_TIMEOUT);

        talon.config_kP(0, 0.455, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.config_kI(0, 0.001, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.config_kD(0, 0.000,RobotMap.TALON_COMMAND_TIMEOUT);

        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(11.5, RobotMap.TALON_COMMAND_TIMEOUT);
    }

}

package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.DriveControlCommand;

public class DrivetrainSubsystem extends Subsystem {

    private static final double WHEEL_DIAMETER = 6;
    private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    public DrivetrainSubsystem() {
        this.leftTalons = new TalonSRX[RobotMap.DRIVE_LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.DRIVE_RIGHT_TALON_PORTS.length];

        for (int i = 0; i < this.leftTalons.length; i++) {
            this.leftTalons[i] = new TalonSRX(RobotMap.DRIVE_LEFT_TALON_PORTS[i]);

            if (i > 0) {
                this.leftTalons[i].set(ControlMode.Follower, RobotMap.DRIVE_LEFT_TALON_PORTS[0]);
            }

            this.configureTalon(this.leftTalons[i]);
        }

        for (int i = 0; i < this.rightTalons.length; i++) {
            this.rightTalons[i] = new TalonSRX(RobotMap.DRIVE_RIGHT_TALON_PORTS[i]);

            if (i > 0) {
                this.rightTalons[i].set(ControlMode.Follower, RobotMap.DRIVE_RIGHT_TALON_PORTS[0]);
            }

            this.rightTalons[i].setInverted(true);
            this.configureTalon(this.rightTalons[i]);
        }
    }

    public void drive(double leftOutput, double rightOutput) {
        this.drive(ControlMode.PercentOutput, leftOutput, rightOutput);
    }

    public void drive(ControlMode controlMode, double leftOutput, double rightOutput) {
        this.leftTalons[0].set(controlMode, leftOutput);
        this.rightTalons[0].set(controlMode, rightOutput);
    }

    public void resetPosition() {
        this.leftTalons[0].setSelectedSensorPosition(0, 0, RobotMap.TALON_COMMAND_TIMEOUT);
        this.rightTalons[0].setSelectedSensorPosition(0, 0, RobotMap.TALON_COMMAND_TIMEOUT);
    }

    public double getLeftPosition() {
        return this.leftTalons[0].getSelectedSensorPosition(0);
    }

    public double getRightPosition() {
        return this.rightTalons[0].getSelectedSensorPosition(0);
    }

    public int getLeftEncoderPosition() {
        return this.leftTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public int getRightEncoderPosition() {
        return this.rightTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public double getLeftError() {
        return this.leftTalons[0].getClosedLoopError(0);
    }

    public double getRightError() {
        return this.rightTalons[0].getClosedLoopError(0);
    }

    public void stop() {
        this.drive(0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new DriveControlCommand());
    }

    public static double distanceToRotations(double distance) {
        return distance / WHEEL_CIRCUMFERENCE;
    }

    public static double rotationsToDistance(double rotations) {
        return rotations * WHEEL_CIRCUMFERENCE;
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configNominalOutputReverse(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputForward(1, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputReverse(-1, RobotMap.TALON_COMMAND_TIMEOUT);

        talon.configMotionAcceleration(500, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configMotionCruiseVelocity(800, RobotMap.TALON_COMMAND_TIMEOUT);
    }

}

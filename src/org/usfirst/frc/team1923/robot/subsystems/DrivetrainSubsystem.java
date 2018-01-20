package org.usfirst.frc.team1923.robot.subsystems;

import org.usfirst.frc.team1923.robot.Constants;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.RawDriveCommand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    private static final double WHEEL_DIAMETER = 6;
    private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

    private TalonSRX[] leftTalons = new TalonSRX[RobotMap.LEFT_DRIVE_PORTS.length];
    private TalonSRX[] rightTalons = new TalonSRX[RobotMap.RIGHT_DRIVE_PORTS.length];
    private PigeonIMU imu;

    private ControlMode controlMode = ControlMode.Velocity;

    public DrivetrainSubsystem() {
        int id = 0;
        for (int i = 0; i < this.leftTalons.length; ++i) {
            this.leftTalons[i] = new TalonSRX(RobotMap.LEFT_DRIVE_PORTS[i]);
            if (i > 0) {
                this.leftTalons[i].set(ControlMode.Follower, id);
            } else {
                id = this.leftTalons[0].getDeviceID();
            }
        }
        for (int i = 0; i < this.rightTalons.length; ++i) {
            this.rightTalons[i] = new TalonSRX(RobotMap.RIGHT_DRIVE_PORTS[i]);
            if (i > 0) {
                this.rightTalons[i].set(ControlMode.Follower, id);
            } else {
                id = this.rightTalons[0].getDeviceID();
            }
        }
    }

    public void drive(double left, double right) {
        this.leftTalons[0].set(this.controlMode, left);
        this.rightTalons[0].set(this.controlMode, right);
    }

    public void setControlMode(ControlMode mode) {
        this.controlMode = mode;
    }

    public void stop() {
        drive(0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RawDriveCommand());
    }

    public static double distanceToRotations(double distance) {
        return distance / WHEEL_CIRCUMFERENCE * Math.PI;
    }

    private double rotationsToDistance(double rotations) {
        return rotations * WHEEL_CIRCUMFERENCE;
    }

    /**
     * Resets current position of the encoders. Since SRX Mag encoders are used in
     * relative mode, this allows us to simplify autons by resetting the home
     * position of the robot.
     */
    public void resetPosition() {
        this.leftTalons[0].setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        this.rightTalons[0].setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    }

    public double getLeftPosition() {
        return this.leftTalons[0].getSelectedSensorPosition(Constants.kPIDLoopIdx);
    }

    public double getRightPosition() {
        return this.rightTalons[0].getSelectedSensorPosition(Constants.kPIDLoopIdx);
    }

    public int getLeftEncPosition() {
        return this.leftTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public int getRightEncPosition() {
        return this.rightTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public double getLeftError() {
        return this.leftTalons[0].getClosedLoopError(Constants.kPIDLoopIdx);
    }

    public double getRightError() {
        return this.rightTalons[0].getClosedLoopError(Constants.kPIDLoopIdx);
    }

    public void configMM() {
        this.leftTalons[0].configMotionAcceleration(500, Constants.kTimeoutMs);
        this.rightTalons[0].configMotionAcceleration(500, Constants.kTimeoutMs);

        this.leftTalons[0].configMotionCruiseVelocity(800, Constants.kTimeoutMs);
        this.rightTalons[0].configMotionCruiseVelocity(800, Constants.kTimeoutMs);
    }

    public PigeonIMU getIMU() {
        return this.imu;
    }
}
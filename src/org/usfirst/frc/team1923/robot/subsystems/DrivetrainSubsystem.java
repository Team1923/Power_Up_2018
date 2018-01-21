package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.RawDriveCommand;

public class DrivetrainSubsystem extends Subsystem {

    private static final double WHEEL_DIAMETER = 6;
    private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    private ControlMode controlMode;

    private double leftOutput;
    private double rightOutput;

    public DrivetrainSubsystem() {
        this.leftTalons = new TalonSRX[RobotMap.LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.RIGHT_TALON_PORTS.length];

        this.controlMode = ControlMode.Velocity;

        for (int i = 0; i < this.leftTalons.length; i++) {
            this.leftTalons[i] = new TalonSRX(RobotMap.LEFT_TALON_PORTS[i]);

            if (i > 0) {
                this.leftTalons[i].set(ControlMode.Follower, RobotMap.LEFT_TALON_PORTS[0]);
            }
        }

        for (int i = 0; i < this.rightTalons.length; i++) {
            this.rightTalons[i] = new TalonSRX(RobotMap.RIGHT_TALON_PORTS[i]);

            if (i > 0) {
                this.rightTalons[i].set(ControlMode.Follower, RobotMap.RIGHT_TALON_PORTS[0]);
            }
        }

        // TODO: Reverse one set of Talons
    }

    public void drive(double leftOutput, double rightOutput) {
        this.leftTalons[0].set(this.controlMode, leftOutput);
        this.rightTalons[0].set(this.controlMode, rightOutput);

        this.leftOutput = leftOutput;
        this.rightOutput = rightOutput;
    }

    public void setControlMode(ControlMode mode) {
        this.controlMode = mode;

        this.drive(this.leftOutput, this.rightOutput);
    }

    public void resetPosition() {
        this.leftTalons[0].setSelectedSensorPosition(0, 0, 10);
        this.rightTalons[0].setSelectedSensorPosition(0, 0, 10);
    }

    public double getLeftPosition() {
        return this.leftTalons[0].getSelectedSensorPosition(0);

    }

    public double getRightPosition() {
        return this.rightTalons[0].getSelectedSensorPosition(0);
    }

    public int getLeftEncPosition() {
        return this.leftTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public int getRightEncPosition() {
        return this.rightTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public double getLeftError() {
        return this.leftTalons[0].getClosedLoopError(0);
    }

    public double getRightError() {
        return this.rightTalons[0].getClosedLoopError(0);
    }

    public void configureMotionMagic() {
        this.leftTalons[0].configMotionAcceleration(500, 10);
        this.rightTalons[0].configMotionAcceleration(500, 10);

        this.leftTalons[0].configMotionCruiseVelocity(800, 10);
        this.rightTalons[0].configMotionCruiseVelocity(800, 10);
    }

    public void stop() {
        drive(0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new RawDriveCommand());
    }

    private double distanceToRotations(double distance) {
        return distance / WHEEL_CIRCUMFERENCE;
    }

    private double rotationsToDistance(double rotations) {
        return rotations * WHEEL_CIRCUMFERENCE;
    }

}

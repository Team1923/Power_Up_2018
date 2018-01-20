package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    private static final int WHEEL_DIAMETER = 6; // inches

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    public DrivetrainSubsystem() {
        // Initialize talons
        this.leftTalons = new TalonSRX[3];
        this.rightTalons = new TalonSRX[3];
    }

    public void drive(double left, double right) {
        this.leftTalons[0].set(this.leftTalons[0].getControlMode(), left);
        this.rightTalons[0].set(this.rightTalons[0].getControlMode(), right);
    }

    public void setControlMode(ControlMode mode) {
        setControlMode(mode);
    }

    public void stop() {
        drive(0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new RawDriveCommand());
    }

    private double distanceToRotations(double distance) {
        return distance / (Math.PI * WHEEL_DIAMETER);
    }

    private double rotationsToDistance(double rotations) {
        return (Math.PI * WHEEL_DIAMETER) * rotations;
    }

}

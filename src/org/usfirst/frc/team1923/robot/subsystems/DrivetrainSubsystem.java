package org.usfirst.frc.team1923.robot.subsystems;

import org.usfirst.frc.team1923.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    private static final int WHEEL_DIAMETER = 6; // inches

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    public DrivetrainSubsystem() {
        // Initialize talons
        this.leftTalons = new TalonSRX[RobotMap.LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.LEFT_TALON_PORTS.length];

        for (int i = 0; i < leftTalons.length; i++) {
            leftTalons[i] = new TalonSRX(i + 1);
        }
        for (int i = 3; i < leftTalons.length; i++) {
            rightTalons[i] = new TalonSRX(i + 1);
        }
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

package org.usfirst.frc.team1923.robot.subsystems;

import org.usfirst.frc.team1923.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    private static final int WHEEL_DIAMETER = 6; // inches
    private ControlMode controlMode = ControlMode.PercentOutput;

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    public DrivetrainSubsystem() {
        // Initialize talons
        this.leftTalons = new TalonSRX[RobotMap.LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.LEFT_TALON_PORTS.length];
        for (int i = 0; i < leftTalons.length; i++) {
            leftTalons[i] = new TalonSRX(RobotMap.LEFT_TALON_PORTS[i]);
        }
        for (int i = 0; i < leftTalons.length; i++) {
            rightTalons[i] = new TalonSRX(RobotMap.RIGHT_TALON_PORTS[i]);
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
        // setDefaultCommand(new RawDriveCommand());
    }

    private double distanceToRotations(double distance) {
        return distance / (Math.PI * WHEEL_DIAMETER);
    }

    private double rotationsToDistance(double rotations) {
        return (Math.PI * WHEEL_DIAMETER) * rotations;
    }

}

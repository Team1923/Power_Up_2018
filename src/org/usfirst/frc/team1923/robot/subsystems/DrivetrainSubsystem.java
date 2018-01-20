package org.usfirst.frc.team1923.robot.subsystems;

import org.usfirst.frc.team1923.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    // all constants will be changed later

    private static final int WHEEL_DIAMETER = 6; // inches

    // Every turn of the encoder equals DRIVE_RATIO turns of the wheel
    private static final double DRIVE_RATIO = 32.5 / 50.0;
    // Middle of wheels measurement in inches
    private static final double DRIVE_BASE_WIDTH = 22.5;
    private static final double DRIVE_CONSTANT = 1;
    private static final double TURNING_CONSTANT = 1.12;

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

        // configPID();

    }

    public void drive(double left, double right) {

    }

    public void setControlMode(ControlMode mode) {

    }

    public void stop() {
        drive(0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new RawDriveCommand());
        // commented because not done yet
    }

    private double distanceToRotations(double distance) {
        return distance / (Math.PI * WHEEL_DIAMETER * DRIVE_RATIO) * DRIVE_CONSTANT;
    }

    private double rotationsToDistance() {
        return 0;
    }

}

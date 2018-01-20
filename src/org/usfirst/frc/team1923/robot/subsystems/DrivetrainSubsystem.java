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
    }

    public void drive(double left, double right) {

    }

    public void setControlMode(ControlMode mode) {

    }

    public void stop() {

    }

    @Override
    protected void initDefaultCommand() {

    }

    private double distanceToRotations() {
        return 0;
    }

    private double rotationsToDistance() {
        return 0;
    }

}

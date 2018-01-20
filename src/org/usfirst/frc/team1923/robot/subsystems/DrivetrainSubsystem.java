package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    private static final int WHEEL_DIAMETER = 6; // inches
    private static final int RIGHT_MASTER_TALON_ID = 1;
    private static final int LEFT_MASTER_TALON_ID = 4;

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    public DrivetrainSubsystem() {
        // Initialize talons
        leftTalons = new TalonSRX[3];
        rightTalons = new TalonSRX[3];

    }

    public void drive(double left, double right) {

        // again, doesn't the input depend on the current mode of the master
        // talon?
        leftTalons[0].set(leftTalons[0].getControlMode(), left);
        rightTalons[0].set(rightTalons[0].getControlMode(), right);

    }

    public void setControlMode(ControlMode mode) {

        leftTalons[0].set(mode, 0); // Doesn't output value depend on the mode?
        rightTalons[0].set(mode, 0);
        for (int c = 1; c < leftTalons.length; c++) {
            leftTalons[c].set((ControlMode.Follower), this.LEFT_MASTER_TALON_ID);
        }
        for (int c = 1; c < rightTalons.length; c++) {
            rightTalons[c].set((ControlMode.Follower), this.RIGHT_MASTER_TALON_ID);
        }

    }

    public void stop() {

        drive(0, 0);

    }

    @Override
    protected void initDefaultCommand() {
        super.setDefaultCommand(new DriveTimeCommand(5, 10));

    }

    private double rotationsToDistance(double rotations) {
        return rotations * (Math.PI * WHEEL_DIAMETER);
    }

    private double distanceToRotations(double distance) {

        return distance / (Math.PI * WHEEL_DIAMETER);
    }

}

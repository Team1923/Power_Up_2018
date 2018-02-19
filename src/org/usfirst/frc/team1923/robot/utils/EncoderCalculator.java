package org.usfirst.frc.team1923.robot.utils;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team1923.robot.RobotMap;

public class EncoderCalculator {

    private double timestamp;

    private int encoderValue;

    private double velocity;
    private double acceleration;

    public EncoderCalculator() {
        this.timestamp = this.getTime();
    }

    public void calculate(int encoderValue) {
        double dt = this.getTime() - this.timestamp;

        if (dt < 0.75) {
            return;
        }

        double dr = (encoderValue - this.encoderValue) / RobotMap.Robot.ENCODER_TICKS_PER_ROTATION;
        double dp = dr * Converter.inchesToFeet(RobotMap.Drivetrain.WHEEL_DIAMETER) * Math.PI;

        double velocity = dp / dt;
        double acceleration = (velocity - this.velocity) / dt;

        this.velocity = velocity;
        this.acceleration = acceleration;
        this.encoderValue = encoderValue;
        this.timestamp = this.getTime();
    }

    public double getVelocity() {
        return this.velocity;
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public double getTime() {
        return Timer.getFPGATimestamp();
    }

}

package org.usfirst.frc.team1923.robot.utils;

import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team1923.robot.RobotMap;

public class EncoderCalculator {

    private double timestamp;

    private double velocity;
    private double acceleration;

    private final double wheelDiameter;

    public EncoderCalculator(double wheelDiameter) {
        this.timestamp = this.getTime();

        this.wheelDiameter = wheelDiameter;
    }

    public void calculate(double tickVelocity) {
        double dt = this.getTime() - this.timestamp;

        double velocity = 10.0 * tickVelocity * this.wheelDiameter * Math.PI / RobotMap.Robot.ENCODER_TICKS_PER_ROTATION;

        this.acceleration = (this.velocity - velocity) / dt;
        this.velocity = velocity;

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

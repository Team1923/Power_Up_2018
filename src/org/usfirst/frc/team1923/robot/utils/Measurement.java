package org.usfirst.frc.team1923.robot.utils;

public enum Measurement {

    ROBOT_WHEELBASE_WIDTH(24.33),
    ROBOT_WHEEL_DIAMETER(6.00),

    ROBOT_LENGTH(32.50),
    ROBOT_WIDTH(27.50),

    ROBOT_MAX_VELOCITY(18),
    ROBOT_MAX_ACCELERATION(12);

    public static final double DRIVETRAIN_VELOCIY_CONSTANT = 0.375;

    private double inches;

    private Measurement(double inches) {
        this.inches = inches;
    }

    public double inInches() {
        return this.inches;
    }

    public double inFeet() {
        return this.inches / 12.0;
    }

    public double inMeters() {
        return this.inches * 0.0254;
    }

    public double inCentimeters() {
        return this.inMeters() * 100.0;
    }

    public static double inchesToMeters(double inches) {
        return inches * 0.0254;
    }

}

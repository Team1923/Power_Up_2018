package org.usfirst.frc.team1923.robot;

public enum Measurement {

    ROBOT_WHEELBASE_WIDTH(24.33),
    ROBOT_WHEEL_DIAMETER(6.00),

    ROBOT_LENGTH(32.50),
    ROBOT_WIDTH(27.50),

    ROBOT_MAX_VELOCITY(12),
    ROBOT_MAX_ACCELERATION(6);

    public static final double DRIVETRAIN_VELOCIY_CONSTANT = 250.0 / 1143.0;

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

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
        return inchesToFeet(this.inches);
    }

    public double inMillimeters() {
        return inchesToMillimeters(this.inches);
    }

    public double inCentimeters() {
        return inchesToCentimeters(this.inches);
    }

    public double inMeters() {
        return inchesToMeters(this.inches);
    }

    public static double inchesToFeet(double inches) {
        return inches / 12;
    }

    public static double inchesToMillimeters(double inches) {
        return inches * 25.4;
    }

    public static double inchesToCentimeters(double inches) {
        return inches * 2.54;
    }

    public static double inchesToMeters(double inches) {
        return inches * 0.0254;
    }

    public static double feetToInches(double feet) {
        return feet * 12;
    }

    public static double millimetersToInches(double millimeters) {
        return millimeters / 25.4;
    }

    public static double centimetersToInches(double centimeters) {
        return centimeters / 2.54;
    }

    public static double metersToInches(double meters) {
        return meters / 0.0254;
    }

}

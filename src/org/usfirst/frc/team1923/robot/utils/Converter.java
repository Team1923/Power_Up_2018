package org.usfirst.frc.team1923.robot.utils;

import org.usfirst.frc.team1923.robot.RobotMap;

public abstract class Converter {

    public static double inchesToFeet(double inches) {
        return inches / 12.0;
    }

    public static double inchesToMeters(double inches) {
        return inches * 0.0254;
    }

    public static int inchesToTicks(double inches, double wheelDiameter) {
        return (int) ((inches * RobotMap.Robot.ENCODER_TICKS_PER_ROTATION) / (Math.PI * wheelDiameter));
    }

    public static double ticksToInches(int ticks, double wheelDiameter) {
        return (ticks / RobotMap.Robot.ENCODER_TICKS_PER_ROTATION) * wheelDiameter * Math.PI;
    }

    public static double millimetersToInches(double millimeters) {
        return millimeters / 25.4;
    }

}

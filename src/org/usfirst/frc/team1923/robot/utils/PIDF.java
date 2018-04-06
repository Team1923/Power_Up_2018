package org.usfirst.frc.team1923.robot.utils;

public class PIDF {

    public static final int PRIMARY_LOOP = 0;
    public static final int AUXILIARY_LOOP = 1;

    public static final int TALON_TURN_SLOT = 0;
    public static final int TALON_GYRO_SLOT = 1;
    public static final int TALON_MOTIONPROFILE_SLOT = 2;
    public static final int TALON_MOTIONMAGIC_SLOT = 3;

    private final double p;
    private final double i;
    private final double d;
    private final double f;

    public PIDF(double p, double i, double d, double f) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
    }

    public double getP() {
        return p;
    }

    public double getI() {
        return i;
    }

    public double getD() {
        return d;
    }

    public double getF() {
        return f;
    }

}

package org.usfirst.frc.team1923.robot.utils;

import java.lang.reflect.Field;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * Customized version of PIDController to allow the specification of an IZone.
 */
public class PIDController extends edu.wpi.first.wpilibj.PIDController {

    private Field totalErrorField;

    private double IZone;

    public PIDController(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, double period) {
        super(Kp, Ki, Kd, Kf, source, output, period);

        try {
            this.totalErrorField = edu.wpi.first.wpilibj.PIDController.class.getDeclaredField("m_totalError");
        } catch (NoSuchFieldException e) {
            e.printStackTrace(); // Should never happen
        }
        this.totalErrorField.setAccessible(true);
        this.IZone = 0;
    }

    public PIDController(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
        this(Kp, Ki, Kd, Kf, source, output, edu.wpi.first.wpilibj.PIDController.kDefaultPeriod);
    }

    public PIDController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
        this(Kp, Ki, Kd, 0, source, output, period);
    }

    public PIDController(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
        this(Kp, Ki, Kd, source, output, edu.wpi.first.wpilibj.PIDController.kDefaultPeriod);
    }

    public void setIZone(double iZone) {
        this.IZone = iZone;
    }

    @Override
    protected void calculate() {
        if (this.getError() > this.IZone) {
            this.clearTotalError();
        }

        super.calculate();
    }

    protected void clearTotalError() {
        try {
            this.totalErrorField.setDouble(this, 0);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
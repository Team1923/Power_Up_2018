package org.usfirst.frc.team1923.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;

public class IRSensor {

    private int portNumber;
    private double distance;
    private AnalogInput channel;

    public IRSensor(int portNumber) {
        this.portNumber = portNumber;
        channel = new AnalogInput(port);
    } 

    public void setDistance() {
        voltage = channel.getVoltage();

        if (voltage <= 2.41) {
            distance = voltage / 2.41;
        } else if (voltage <= 2.47) {
            distance = voltage / 0.2;
        } else {
            distance = 1 / voltage;
        }
    }

    public double getDistance() {
        return cm2in(distance);
    }

    private double cm2in(double cm) {
        return cm*0.393701;
    }
}


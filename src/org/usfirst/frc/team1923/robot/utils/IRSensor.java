package org.usfirst.frc.team1923.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;

public class IRSensor {
    private int port;
    private AnalogInput sensor;

    public IRSensor(int port) {
        this.port = port;
        sensor = new AnalogInput(port);
    } 

    public double getDistance() {
        double voltage = sensor.getVoltage();
        double distance; // To be computed 

        // From y=0 to y=2.40, there is an *almost* linear increase in the graph
        // From (1, 2.40) to around (1.4, 2.46), there is a different linear increase
        // From around (1.4, 2.46) to x=∞ there is a decrease along approximately y=1/x
        if (voltage <= 2.4) {
            distance = (voltage / 2.4); // Solving y=2.4x for x
        } else if (voltage <= 2.46) {
            distance = ((1.4 / 2.46) * voltage); // Solving 1.4y = 2.46x for x
        } else {
            distance = (1 / voltage); // Solving y = 1/x for x
        }

        return (distance * 0.393701);
    }
}

package org.usfirst.frc.team1923.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;

public class IRSensor {

    private final static double[] distances = {1, 2, 3, 4, 5};
    private final static double[] voltages = {5, 4, 3, 2, 0.5};

    private AnalogInput input;

    public IRSensor(int port) {
        this.input = new AnalogInput(port);
    }

    public double getDistance() {
        double voltage = input.getVoltage();
        int below = 0;
        int above = voltages.length - 1;
        for (int i = 0; i < voltages.length; ++i) {
            if (voltages[i] > voltages[below] && voltages[i] <= voltage) {
                below = i;
            }
            if (voltages[i] < voltages[above] && voltages[i] >= voltage) {
                above = i;
            }
        }
        return distances[below] + (distances[above] - distances[below]) * (voltage - voltages[below]) / (voltages[above] - voltages[below]);
    }
}

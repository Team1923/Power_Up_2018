package org.usfirst.frc.team1923.robot.utils.battery;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import org.usfirst.frc.team1923.robot.RobotMap;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BatteryMonitor {

    private final double MIN_CURRENT_DEVIATION = 5.00;
    private final double MIN_VOLTAGE_DEVIATION = 1.00;

    private final double MIN_SYSTEM_VOLAGE = 7.00;

    private PowerDistributionPanel pdp;

    private List<PDPReading> pdpReadings;
    private long lastCalculation;

    private final Queue<OutputDevice> outputDevices;

    private double voltage;
    private double resistance;

    private boolean enabled;

    public BatteryMonitor() {
        this.pdp = new PowerDistributionPanel(RobotMap.PDP_PORT);

        this.pdpReadings = new ArrayList<PDPReading>(100);

        this.lastCalculation = System.currentTimeMillis();

        // Initial estimated values
        this.voltage = (this.pdp.getVoltage() + 12.00) / 2;
        this.resistance = 0.025;

        this.outputDevices = new PriorityQueue<OutputDevice>(25);
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void tick() {
        if (!this.enabled) {
            return;
        }

        this.pdpReadings.add(new PDPReading(
                this.pdp.getVoltage(),
                this.pdp.getTotalCurrent()
        ));

        this.calculate();

        synchronized (this.outputDevices) {

        }
    }

    public synchronized double inform(OutputDevice outputDevice) {
        if (!this.enabled) {
            return outputDevice.getOutput();
        }

        if (!this.outputDevices.contains(outputDevice)) {
            this.outputDevices.add(outputDevice);
        }

        // TODO: Calculate the max output of the device. Lower outputs of other devices with lower priority if necessary.

        return 0.00;
    }

    public synchronized void reprioritize() {
        if (!this.enabled) {
            return;
        }

        Queue<OutputDevice> outputDevices = new PriorityQueue<OutputDevice>(this.outputDevices.size());
        outputDevices.addAll(this.outputDevices);

        this.outputDevices.clear();
        this.outputDevices.addAll(outputDevices);
    }

    private void calculate() {
        if (this.lastCalculation + 2500 > System.currentTimeMillis()) {
            return;
        }

        this.lastCalculation = System.currentTimeMillis();

        double[] x = new double[this.pdpReadings.size()];
        double[] y = new double[this.pdpReadings.size()];

        int index = 0;

        for (PDPReading pdpReading : this.pdpReadings) {
            x[index] = pdpReading.getCurrent();
            y[index] = pdpReading.getVoltage();

            index++;
        }

        LinearRegression linearRegression = new LinearRegression(x, y);

        if (linearRegression.getDeviationX() < MIN_CURRENT_DEVIATION || linearRegression.getDeviationY() < MIN_VOLTAGE_DEVIATION) {
            // Do not clear PDP readings, hope for more accuracy next time
            return;
        }

        this.voltage = linearRegression.getIntercept();
        this.resistance = -linearRegression.getSlope();
    }

    private class PDPReading {

        private long timestamp;

        private double voltage;
        private double current;

        public PDPReading(double voltage, double current) {
            this.voltage = voltage;
            this.current = current;

            this.timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getVoltage() {
            return voltage;
        }

        public double getCurrent() {
            return current;
        }

    }

}

package org.usfirst.frc.team1923.robot.utils.battery;

import org.usfirst.frc.team1923.robot.Robot;

public abstract class OutputDevice implements Comparable {

    protected double output;
    protected int priority;

    public OutputDevice(int priority) {
        this.priority = priority;
    }

    public abstract double getCurrent(double output);

    protected void setOutput(double output) {
        this.output = output;
        this.output = Robot.batteryMonitor.inform(this);
    }

    public double getOutput() {
        return this.output;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof OutputDevice)) {
            return -1;
        }

        OutputDevice od = (OutputDevice) o;

        return Integer.compare(this.getPriority(), od.getPriority());
    }

}

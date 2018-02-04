package org.usfirst.frc.team1923.robot.utils.battery;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import org.usfirst.frc.team1923.robot.RobotMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BatteryMonitor {

    private final double MIN_CURRENT_DEVIATION = 5.00;
    private final double MIN_VOLTAGE_DEVIATION = 1.00;

    private PowerDistributionPanel pdp;

    private List<PDPReading> pdpReadings;
    private long lastCalculation;

    private double voltage;
    private double resistance;

    private final ConcurrentLinkedQueue<QueuedAction> outputQueue;

    public BatteryMonitor() {
        this.pdp = new PowerDistributionPanel(RobotMap.PDP_PORT);

        this.pdpReadings = new ArrayList<PDPReading>(100);

        this.lastCalculation = System.currentTimeMillis();

        // Initial estimated values
        this.voltage = (this.pdp.getVoltage() + 12.00) / 2;
        this.resistance = 0.025;

        this.outputQueue = new ConcurrentLinkedQueue<>();
    }

    public void tick() {
        this.pdpReadings.add(new PDPReading(
                this.pdp.getVoltage(),
                this.pdp.getTotalCurrent()
        ));

        this.calculate();

        synchronized (this.outputQueue) {
            Object[] queuedActions = this.outputQueue.toArray();

            Arrays.sort(queuedActions);

            for (Object object : queuedActions) {
                QueuedAction queuedAction = (QueuedAction) object;
            }

            // TODO: Do output calculations and limit (if needed)
        }
    }

    /**
     * Queue a new output action, takes effect on the next tick (max 20ms delay)
     *
     * @param outputType Enum output type
     * @param outputValue Value of output
     * @param outputCallback Callback class to output
     * @param priority Priority of output action. Lower has higher priority.
     */
    public void queueAction(OutputType outputType, double outputValue, OutputCallback outputCallback, int priority) {
        synchronized (this.outputQueue) {
            this.outputQueue.offer(new QueuedAction(
                    outputType,
                    outputValue,
                    outputCallback,
                    priority
            ));
        }
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

    private class QueuedAction implements Comparable {

        private OutputType outputType;
        private OutputCallback outputCallback;
        private double outputValue;

        private int priority;

        public QueuedAction(OutputType outputType, double outputValue, OutputCallback outputCallback, int priority) {
            this.outputType = outputType;
            this.outputValue = outputValue;
            this.outputCallback = outputCallback;
            this.priority = priority;
        }

        public OutputType getOutputType() {
            return outputType;
        }

        public OutputCallback getOutputCallback() {
            return outputCallback;
        }

        public double getOutputValue() {
            return outputValue;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof QueuedAction)) {
                return -1;
            }

            QueuedAction queuedAction = (QueuedAction) o;

            return Integer.compare(queuedAction.getPriority(), this.getPriority());
        }

    }

}

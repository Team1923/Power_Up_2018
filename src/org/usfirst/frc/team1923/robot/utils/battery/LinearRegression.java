package org.usfirst.frc.team1923.robot.utils.battery;

public class LinearRegression {

    private double slope;
    private double intercept;

    private double deviationX;
    private double deviationY;

    public LinearRegression(double[] x, double[] y) {
        this.calculateRegression(x, y);

        this.deviationX = this.calculateDeviation(x);
        this.deviationY = this.calculateDeviation(y);
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getDeviationX() {
        return deviationX;
    }

    public double getDeviationY() {
        return deviationY;
    }

    private void calculateRegression(double[] x, double[] y) {
        double averageX = sum(x) / x.length;
        double averageY = sum(y) / y.length;

        double averageXX = 0;
        double averageXY = 0;

        for (int i = 0; i < x.length; i++) {
            averageXX += Math.pow(x[i] - averageX, 2);
            averageXY += (x[i] - averageX) * (y[i] - averageY);
        }

        this.slope = averageXY / averageXX;
        this.intercept = averageY - this.slope * averageX;
    }

    private double calculateDeviation(double[] input) {
        double average = sum(input) / input.length;

        double variance = 0;

        for (double d : input) {
            variance = Math.pow(d - average, 2);
        }

        variance = variance / input.length;

        return Math.sqrt(variance);
    }

    private static double sum(double[] array) {
        double sum = 0;

        for (double d : array) {
            sum += d;
        }

        return sum;
    }

}

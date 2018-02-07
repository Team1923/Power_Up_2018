package org.usfirst.frc.team1923.robot.utils.battery;

import java.util.Arrays;

public class AveragingFilter {
	
	private int N;
	private double[] circuitBuff;
	private double total;
	private int index;
	
	public AveragingFilter(int len, double init_val){
		index = 0;
		N = len;
		circuitBuff = new double[N];
		Arrays.fill(circuitBuff, init_val);
		total = N * init_val;
	}
	
	public double filter(double input){
		total -= circuitBuff[index];
		circuitBuff[index] = input;
		index = (index + 1) % N;
		return total/N;
	}
	
}

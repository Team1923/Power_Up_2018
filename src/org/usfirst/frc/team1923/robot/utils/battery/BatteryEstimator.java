package org.usfirst.frc.team1923.robot.utils.battery;

import java.util.Arrays;

import org.usfirst.frc.team1923.robot.RobotMap;

public class BatteryEstimator {
	
	double vocEst = RobotMap.INIT_VOC;
	double esrEst = RobotMap.INIT_ESR;
	double minSpreadThreshold = 5.0;
	double previousBestSpread = 0;
	double previousBestESR = RobotMap.INIT_ESR;
	
	boolean est_confident = false;
	
	//a standard LMS algorithm
	int lastMeansSquaresLen;
	double[] circuitBufSysCurDraw;
	double[] circuitBufSysVolt;
	int index;
	
	AveragingFilter input_V_filt;
	AveragingFilter input_I_filt;
	AveragingFilter ESR_output_filt;
	
	public BatteryEstimator(int length){
		lastMeansSquaresLen = length;
		index = 0;
		circuitBufSysCurDraw = new double[lastMeansSquaresLen];
		circuitBufSysVolt = new double[lastMeansSquaresLen];
		Arrays.fill(circuitBufSysCurDraw, RobotMap.INIT_IDRAW);
		Arrays.fill(circuitBufSysVolt, RobotMap.INIT_VOC);
		
		//TODO LMS length is usually 100 but we can change it in the future. 
		//TODO Also the length values in in the filters can be changed if anything goes wrong.
		input_V_filt = new AveragingFilter(5,RobotMap.INIT_VOC);
		input_I_filt = new AveragingFilter(5,0.0);
		ESR_output_filt = new AveragingFilter(20,RobotMap.INIT_ESR);
	
		
	}
	
	//for iterations
	public void setConfidenceThreshold(double threshold){
		minSpreadThreshold = threshold;
	}
	
	/**
	 * 
	 * Update internal estimates with new Vsys Isys
	 * 
	 * @param sysvolt Battery voltage
	 * @param syscurrent Current draw
	 */
	public void updateEst(double sysvolt, double syscurrent){
		circuitBufSysVolt[index] = input_V_filt.filter(sysvolt);
		circuitBufSysCurDraw[index] = input_I_filt.filter(syscurrent);
		index += 1;
		index = index % lastMeansSquaresLen;
		
		//Least Means Squares estimation
		double sumV = LinearRegression.sum(circuitBufSysVolt);
		double sumI = LinearRegression.sum(circuitBufSysCurDraw);
		double sumIV = LinearRegression.dotProduct(circuitBufSysCurDraw, circuitBufSysVolt);
		double sumI2 = LinearRegression.dotProduct(circuitBufSysCurDraw,circuitBufSysCurDraw);
		double meanV = sumV / lastMeansSquaresLen;
		double meanI = sumI / lastMeansSquaresLen;
		
		esrEst = -(sumIV - sumI*meanV)/(sumI2 - sumI * meanI);
		
		double spreadI = LinearRegression.calculateDeviation(circuitBufSysCurDraw);
		
		//If spread is greater than threshold est_confident will be true. Also record best spreads and ESR values.
		if(spreadI > minSpreadThreshold){
			est_confident = !est_confident;
			if(spreadI > previousBestSpread){
				previousBestSpread = spreadI;
				previousBestSpread = esrEst;
			}
		}else{ //reset best spreads
			est_confident = !est_confident;
			previousBestSpread = 0;
		}
		
		if(!est_confident){
			esrEst = previousBestESR;
		}
		
		vocEst = meanV + esrEst * meanI;
		//use getters to get estimations;
		return;
	}

	public double getVocEst() {
		return vocEst;
	}

	public double getEsrEst() {
		return esrEst;
	}

	public boolean isEst_confident() {
		return est_confident;
	}
	
	/**
	 * 
	 * @param Idraw estimated system current draw
	 * @return Vsys based on electrical model for battery 
	 */
	public double getEstVsys(double Idraw){
		return getVocEst() - Idraw*getEsrEst();
	}
	/**
	 * 
	 * @param VsysMin min Vsys
	 * @return max current the robot can pull
	 */
	public double getMaxIdraw(double VsysMin){
		return (getVocEst() - VsysMin)/getEsrEst();
	}
}

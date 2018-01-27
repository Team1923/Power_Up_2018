package org.usfirst.frc.team1923.robot.subsystems;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorDistanceCommand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

//need to know more about the hardware 
//also need to know how to find the "switches" libby talked about 
// that would allow us to see if the encoder reached a min/max pos
public class ElevatorSubsystem extends Subsystem {
	
	private TalonSRX left;
	private TalonSRX right;

	private ControlMode controlMode;
	
	
	public ElevatorSubsystem() {
		
		left = new TalonSRX(RobotMap.LEFT_ELEVATOR_PORT);
		right = new TalonSRX(RobotMap.RIGHT_ELEVATOR_PORT);
		this.controlMode = ControlMode.PercentOutput;
		
		// resetEncoders();
		
	}
	
	
	public void raise(double power) {
		// TODO check position of elevator 
		// if position is max, dont do anything
		
		this.left.set(this.controlMode, power);
		this.right.set(this.controlMode, power);
	}
	
	
	public void lower(double power ) {
		// TODO check position of elevator 
		// if position is min, dont do anything
		
		this.left.set(this.controlMode, -power);
		this.right.set(this.controlMode, power);
	}

	public void resetEncoders() {
		
	}
	
	public double getElevatorPosition() {
		return 0.0;
		
	}
	
	// stops the elevator at whatever position its at
	public void stop() {
		this.left.set(this.controlMode, 0);
		this.right.set(this.controlMode, 0);
	}
	
	// TODO fix
    @Override
    protected void initDefaultCommand() {

		setDefaultCommand(new ElevatorDistanceCommand(0));
    }

}

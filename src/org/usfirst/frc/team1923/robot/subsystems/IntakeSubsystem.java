package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import javax.management.timer.Timer;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.RawDriveCommand;
import org.usfirst.frc.team1923.robot.commands.intake.RawIntakeCommand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class IntakeSubsystem extends Subsystem {

    Ultrasonic sonar;
    DoubleSolenoid leftSolenoid;
    DoubleSolenoid rightSolenoid;
    
    private TalonSRX leftTalon;
    private TalonSRX rightTalon;
    private ControlMode controlMode;

    public IntakeSubsystem() {
        try {
            this.leftSolenoid = new DoubleSolenoid(RobotMap.LEFT_DOUBLESOLENOID_FORWARD_PORT, RobotMap.LEFT_DOUBLESOLENOID_REVERSE_PORT);
            this.rightSolenoid = new DoubleSolenoid(RobotMap.RIGHT_DOUBLESOLENOID_FORWARD_PORT, RobotMap.RIGHT_DOUBLESOLENOID_REVERSE_PORT);
            this.sonar = new Ultrasonic(RobotMap.INTAKE_ULTRASONIC_PING_PORT, RobotMap.INTAKE_ULTRASONIC_ECHO_PORT, Unit.kInches);
            this.sonar.setEnabled(true);
            this.sonar.setAutomaticMode(true);

            this.leftTalon = new TalonSRX(RobotMap.LEFT_INTAKE_TALON_PORT);
            this.rightTalon = new TalonSRX(RobotMap.RIGHT_INTAKE_TALON_PORT);

            this.controlMode = ControlMode.PercentOutput;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickupItem() {
        this.leftSolenoid.set(DoubleSolenoid.Value.kReverse);
        this.rightSolenoid.set(DoubleSolenoid.Value.kReverse);
        //Set power to Talons;
        this.leftTalon.set(this.controlMode, 111);
        this.rightTalon.set(this.controlMode, 111);

        while(sonar.getRangeInches() >= 2) {}

        this.leftTalon.set(this.controlMode, 0);
        this.rightTalon.set(this.controlMode, 0);

        this.leftSolenoid.set(DoubleSolenoid.Value.kForward);
        this.rightSolenoid.set(DoubleSolenoid.Value.kForward);
    }

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RawIntakeCommand());
	}
	
	public void stop() {
		this.leftTalon.set(this.controlMode, 0);
        this.rightTalon.set(this.controlMode, 0);
        this.leftSolenoid.set(DoubleSolenoid.Value.kOff);
        this.rightSolenoid.set(DoubleSolenoid.Value.kOff);
	}
}

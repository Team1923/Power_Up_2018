package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeControlCommand;

public class IntakeSubsystem extends Subsystem {

    private DoubleSolenoid intakePositionSolenoid;

    private TalonSRX leftTalon;
    private TalonSRX rightTalon;

    public IntakeSubsystem() {
        this.intakePositionSolenoid = new DoubleSolenoid(RobotMap.Robot.PCM_PORT, RobotMap.Intake.SOLENOID_RAISE_PORT, RobotMap.Intake.SOLENOID_LOWER_PORT);

        this.leftTalon = new TalonSRX(RobotMap.Intake.LEFT_TALON_PORT);
        this.rightTalon = new TalonSRX(RobotMap.Intake.RIGHT_TALON_PORT);
        this.configureTalon(this.leftTalon);
        this.configureTalon(this.rightTalon);
    }

    public void raise() {
        this.intakePositionSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void lower() {
        this.intakePositionSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void intake(double speed) {
        this.intakeLeft(speed);
        this.intakeRight(speed);
    }

    public void intakeLeft(double speed) {
        this.leftTalon.set(ControlMode.PercentOutput, speed);
    }

    public void intakeRight(double speed) {
        this.rightTalon.set(ControlMode.PercentOutput, speed);
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new IntakeControlCommand());
    }

    public void stop() {
        this.intake(0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Intake Position", this.intakePositionSolenoid.get() == DoubleSolenoid.Value.kForward);
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configNominalOutputReverse(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputForward(1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputReverse(-1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        talon.setInverted(true);
    }

}

package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.usfirst.frc.team1923.robot.commands.intake.RawIntakeCommand;

public class IntakeSubsystem extends Subsystem {

    private Ultrasonic leftUltrasonic;
    private Ultrasonic rightUltrasonic;
    private long startTime;
    private boolean secure;

    private DoubleSolenoid solenoid;
    
    private TalonSRX leftTalon;
    private TalonSRX rightTalon;
    private ControlMode controlMode;

    public IntakeSubsystem() {
        this.solenoid = new DoubleSolenoid(RobotMap.PCM_MODULE_PORT, RobotMap.INTAKE_SOLENOID_FORWARD_PORT, RobotMap.INTAKE_SOLENOID_REVERSE_PORT);

        this.leftTalon = new TalonSRX(RobotMap.INTAKE_LEFT_TALON_PORT);
        this.rightTalon = new TalonSRX(RobotMap.INTAKE_RIGHT_TALON_PORT);
        this.configureTalon(this.leftTalon);
        this.configureTalon(this.rightTalon);

        this.startTime = 0;
        this.secure = false;

        this.controlMode = ControlMode.PercentOutput;

        this.leftUltrasonic = new Ultrasonic(RobotMap.INTAKE_LEFT_ULTRASONIC_PING_PORT, RobotMap.INTAKE_LEFT_ULTRASONIC_ECHO_PORT);
        this.rightUltrasonic = new Ultrasonic(RobotMap.INTAKE_RIGHT_ULTRASONIC_PING_PORT, RobotMap.INTAKE_RIGHT_ULTRASONIC_ECHO_PORT);
        this.configureUltrasonic(this.leftUltrasonic);
        this.configureUltrasonic(this.rightUltrasonic);
    }

    public void open() {
        this.solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void close() {
        this.solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void intake(double speed) {
        this.intakeLeft(speed);
        this.intakeRight(speed);
    }

    public void intakeLeft(double speed) {
        this.leftTalon.set(this.controlMode, speed);
    }

    public void intakeRight(double speed) {
        this.rightTalon.set(this.controlMode, speed);
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new RawIntakeCommand());
    }
    
    public void stop() {
        this.leftTalon.set(this.controlMode, 0);
        this.rightTalon.set(this.controlMode, 0);
    }

    public void refreshSensors() {
        if (this.leftUltrasonic.getRangeInches() < 2.0 && this.rightUltrasonic.getRangeInches() < 2.0) {
            if (this.startTime == 0) {
                this.startTime = System.currentTimeMillis();
            } else if (this.startTime + 750L < System.currentTimeMillis()) {
                this.secure = true;
            }
        } else {
            this.startTime = 0;
            this.secure = false;
        }
    }

    public boolean isSecure() {
        return this.secure;
    }

    private void configureUltrasonic(Ultrasonic ultrasonic) {
        ultrasonic.setEnabled(true);
        ultrasonic.setAutomaticMode(true);
        ultrasonic.setDistanceUnits(Unit.kInches);
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configNominalOutputReverse(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputForward(1.0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputReverse(-1.0, RobotMap.TALON_COMMAND_TIMEOUT);
    }

}

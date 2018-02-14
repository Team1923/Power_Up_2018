package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeControlCommand;

public class IntakeSubsystem extends Subsystem {

    private static final int SECURE_THRESHOLD = 750;

    private Ultrasonic leftUltrasonic;
    private Ultrasonic rightUltrasonic;
    private long startTime;

    private DoubleSolenoid solenoid;

    private TalonSRX leftTalon;
    private TalonSRX rightTalon;

    public IntakeSubsystem() {
        // this.solenoid = new DoubleSolenoid(RobotMap.PCM_MODULE_PORT, RobotMap.INTAKE_SOLENOID_FORWARD_PORT, RobotMap.INTAKE_SOLENOID_REVERSE_PORT);

        this.leftTalon = new TalonSRX(RobotMap.INTAKE_LEFT_TALON_PORT);
        this.rightTalon = new TalonSRX(RobotMap.INTAKE_RIGHT_TALON_PORT);
        this.configureTalon(this.leftTalon);
        this.configureTalon(this.rightTalon);

        this.startTime = 0;

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
        this.leftTalon.set(ControlMode.PercentOutput, 0);
        this.rightTalon.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic() {
        if (this.leftUltrasonic.getRangeInches() < 2 && this.rightUltrasonic.getRangeInches() < 2) {
            if (this.startTime == 0) {
                this.startTime = System.currentTimeMillis();
            }
        } else {
            this.startTime = 0;
        }
    }

    public boolean isSecure() {
        return this.startTime + SECURE_THRESHOLD < System.currentTimeMillis();
    }

    private void configureUltrasonic(Ultrasonic ultrasonic) {
        ultrasonic.setEnabled(true);
        ultrasonic.setAutomaticMode(true);
        ultrasonic.setDistanceUnits(Ultrasonic.Unit.kInches);
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configNominalOutputReverse(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputForward(1, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputReverse(-1, RobotMap.TALON_COMMAND_TIMEOUT);
    }

}

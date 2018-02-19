package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeControlCommand;

public class IntakeSubsystem extends Subsystem {

    private Ultrasonic leftUltrasonic;
    private Ultrasonic rightUltrasonic;
    private long startTime;

    private DoubleSolenoid intakeArmSolenoid;
    private DoubleSolenoid intakePositionSolenoid;

    private TalonSRX leftTalon;
    private TalonSRX rightTalon;

    public IntakeSubsystem() {
        this.intakeArmSolenoid = new DoubleSolenoid(RobotMap.Robot.PCM_PORT, RobotMap.Intake.SOLENOID_OPEN_PORT, RobotMap.Intake.SOLENOID_CLOSE_PORT);
        this.intakePositionSolenoid = new DoubleSolenoid(RobotMap.Robot.PCM_PORT, RobotMap.Intake.SOLENOID_RAISE_PORT, RobotMap.Intake.SOLENOID_LOWER_PORT);

        this.leftTalon = new TalonSRX(RobotMap.Intake.LEFT_TALON_PORT);
        this.rightTalon = new TalonSRX(RobotMap.Intake.RIGHT_TALON_PORT);
        this.configureTalon(this.leftTalon);
        this.configureTalon(this.rightTalon);

        this.leftTalon.setInverted(true);

        this.startTime = 0;

        this.leftUltrasonic = new Ultrasonic(RobotMap.Intake.LEFT_ULTRASONIC_PING_PORT, RobotMap.Intake.LEFT_ULTRASONIC_ECHO_PORT);
        this.rightUltrasonic = new Ultrasonic(RobotMap.Intake.RIGHT_ULTRASONIC_PING_PORT, RobotMap.Intake.RIGHT_ULTRASONIC_ECHO_PORT);
        this.configureUltrasonic(this.leftUltrasonic);
        this.configureUltrasonic(this.rightUltrasonic);
    }

    public void open() {
        this.intakeArmSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void close() {
        this.intakeArmSolenoid.set(DoubleSolenoid.Value.kReverse);
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
        if (this.leftUltrasonic.getRangeInches() < 2 && this.rightUltrasonic.getRangeInches() < 2) {
            if (this.startTime == 0) {
                this.startTime = System.currentTimeMillis();
            }
        } else {
            this.startTime = 0;
        }
    }

    private void configureUltrasonic(Ultrasonic ultrasonic) {
        ultrasonic.setEnabled(true);
        ultrasonic.setAutomaticMode(true);
        ultrasonic.setDistanceUnits(Ultrasonic.Unit.kInches);
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configNominalOutputReverse(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputForward(1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        talon.configPeakOutputReverse(-1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

}

package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorControlCommand;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.EncoderCalculator;
import org.usfirst.frc.team1923.robot.utils.PIDF;

public class ElevatorSubsystem extends Subsystem {

    private TalonSRX[] talons;

    private EncoderCalculator encoderCalculator;

    public ElevatorSubsystem() {
        this.talons = new TalonSRX[RobotMap.Elevator.TALON_PORTS.length];

        for (int i = 0; i < this.talons.length; i++) {
            this.talons[i] = new TalonSRX(RobotMap.Elevator.TALON_PORTS[i]);

            this.talons[i].configNominalOutputForward(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            this.talons[i].configNominalOutputReverse(0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            this.talons[i].configPeakOutputForward(1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            this.talons[i].configPeakOutputReverse(-1, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            this.talons[i].configMotionAcceleration(Converter.inchesToTicks(RobotMap.Elevator.MM_MAX_ACCELERATION, RobotMap.Elevator.PULLEY_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            this.talons[i].configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Elevator.MM_MAX_VELOCITY, RobotMap.Elevator.PULLEY_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

            this.talons[i].enableVoltageCompensation(true);
            this.talons[i].configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

            if (i > 0) {
                this.talons[i].set(ControlMode.Follower, RobotMap.Elevator.TALON_PORTS[0]);
            }
        }

        this.talons[0].overrideLimitSwitchesEnable(true);
        this.talons[0].configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Elevator.TALON_PORTS[1], RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Elevator.TALON_PORTS[1], RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.talons[0].configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PIDF.PRIMARY_LOOP, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].setSensorPhase(false);


        this.talons[0].config_kP(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Elevator.MM_PIDF.getP(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kI(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Elevator.MM_PIDF.getI(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kD(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Elevator.MM_PIDF.getD(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kF(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Elevator.MM_PIDF.getF(), RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].configAllowableClosedloopError(PIDF.TALON_MOTIONMAGIC_SLOT, RobotMap.Elevator.MM_ALLOWABLE_ERROR, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.encoderCalculator = new EncoderCalculator(RobotMap.Elevator.PULLEY_DIAMETER);
    }

    @Override
    public void periodic() {
        this.encoderCalculator.calculate(this.talons[0].getSelectedSensorVelocity(PIDF.PRIMARY_LOOP));

        SmartDashboard.putNumber("Elevator Encoder", this.getEncoderPosition());
        SmartDashboard.putNumber("Elevator Position", this.getElevatorPosition());

        SmartDashboard.putBoolean("Rev LMT", this.talons[1].getSensorCollection().isRevLimitSwitchClosed());
        SmartDashboard.putBoolean("Fwd LMT", this.talons[1].getSensorCollection().isFwdLimitSwitchClosed());
    }

    public boolean getForwardLimitSwitch() {
        return this.talons[1].getSensorCollection().isFwdLimitSwitchClosed();
    }

    public double getElevatorPosition() {
        return Converter.ticksToInches(this.getEncoderPosition(), RobotMap.Elevator.PULLEY_DIAMETER);
    }

    public int getEncoderPosition() {
        return this.talons[0].getSelectedSensorPosition(PIDF.PRIMARY_LOOP);
    }

    public void stop() {
        this.set(ControlMode.PercentOutput, 0);
    }

    public void set(ControlMode controlMode, double value) {
        this.talons[0].set(controlMode, value);
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new ElevatorControlCommand());
    }

}

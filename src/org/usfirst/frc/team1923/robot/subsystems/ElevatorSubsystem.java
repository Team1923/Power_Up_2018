package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorControlCommand;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.EncoderCalculator;

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
            this.talons[i].configMotionAcceleration(Converter.inchesToTicks(RobotMap.Elevator.MAX_ACCELERATION, RobotMap.Elevator.PULLEY_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
            this.talons[i].configMotionCruiseVelocity(Converter.inchesToTicks(RobotMap.Elevator.MAX_VELOCITY, RobotMap.Elevator.PULLEY_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

            this.talons[i].enableVoltageCompensation(true);
            this.talons[i].configVoltageCompSaturation(RobotMap.Robot.TALON_NOMINAL_OUTPUT_VOLTAGE, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

            if (i > 0) {
                this.talons[i].set(ControlMode.Follower, RobotMap.Elevator.TALON_PORTS[0]);
            }
        }

        this.talons[0].overrideLimitSwitchesEnable(true);
        this.talons[0].configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.talons[0].configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.talons[0].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.talons[0].config_kP(0, RobotMap.Elevator.P, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kI(0, RobotMap.Elevator.I, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kD(0, RobotMap.Elevator.D, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kF(0, RobotMap.Elevator.F, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].configAllowableClosedloopError(0, RobotMap.Elevator.ALLOWABLE_ERROR, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.encoderCalculator = new EncoderCalculator();
    }

    @Override
    public void periodic() {
        this.encoderCalculator.calculate(this.talons[0].getSelectedSensorPosition(0));
    }

    public double getElevatorVelocity() {
        return this.encoderCalculator.getVelocity() * 12;
    }

    public double getElevatorPosition() {
        return Converter.ticksToInches(this.talons[0].getSelectedSensorPosition(0), RobotMap.Elevator.PULLEY_DIAMETER);
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

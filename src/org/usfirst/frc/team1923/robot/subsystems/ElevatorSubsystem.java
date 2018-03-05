package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.Robot;
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
        this.talons[0].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].setSensorPhase(false);


        this.talons[0].config_kP(0, RobotMap.Elevator.MM_P, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kI(0, RobotMap.Elevator.MM_I, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kD(0, RobotMap.Elevator.MM_D, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].config_kF(0, RobotMap.Elevator.MM_F, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        this.talons[0].configAllowableClosedloopError(0, RobotMap.Elevator.MM_ALLOWABLE_ERROR, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);

        this.encoderCalculator = new EncoderCalculator(RobotMap.Elevator.PULLEY_DIAMETER);

//        Robot.logger.addDataSource("Elevator_Encoder", () -> this.getEncoderPosition() + "");
//        Robot.logger.addDataSource("Elevator_Velocity", () -> String.format("%.4g%n", this.encoderCalculator.getVelocity()));
//        Robot.logger.addDataSource("Elevator_Accel", () -> String.format("%.4g%n", this.encoderCalculator.getAcceleration()));
//        Robot.logger.addDataSource("Elevator_TalonMode", () -> this.talons[0].getControlMode().name());
    }

    @Override
    public void periodic() {
        this.encoderCalculator.calculate(this.talons[0].getSelectedSensorVelocity(0));

        SmartDashboard.putNumber("Elevator Velocity", this.encoderCalculator.getVelocity());
        SmartDashboard.putNumber("Elevator Encoder", this.getEncoderPosition());
        SmartDashboard.putNumber("Elevator Position", this.getElevatorPosition());

        SmartDashboard.putBoolean("Rev LMT", this.talons[1].getSensorCollection().isRevLimitSwitchClosed());
        SmartDashboard.putBoolean("Fwd LMT", this.talons[1].getSensorCollection().isFwdLimitSwitchClosed());
    }

    public boolean getForwardLimitSwitch() {
        return this.talons[1].getSensorCollection().isFwdLimitSwitchClosed();
    }

    public double getElevatorVelocity() {
        return this.encoderCalculator.getVelocity() * 12.0;
    }

    public double getElevatorPosition() {
        return Converter.ticksToInches(this.getEncoderPosition(), RobotMap.Elevator.PULLEY_DIAMETER);
    }

    public int getEncoderPosition() {
        return this.talons[0].getSelectedSensorPosition(0);
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

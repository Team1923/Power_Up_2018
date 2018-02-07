								package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import org.usfirst.frc.team1923.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorControlCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorZeroCommand;

public class ElevatorSubsystem extends Subsystem {

    private final double K_P = 0.2000;
    private final double K_I = 0.0001;
    private final double K_D = 0.0000;
    private final double K_F = 0.0000;

    private final int ALLOWABLE_ERROR = 300;
    
    private TalonSRX[] talons;
    private boolean zeroed;
    
    public ElevatorSubsystem() {
        this.talons = new TalonSRX[RobotMap.ELEVATOR_TALON_PORTS.length];

        for (int i = 0; i < this.talons.length; i++) {
            this.talons[i] = new TalonSRX(RobotMap.ELEVATOR_TALON_PORTS[i]);
            this.talons[i].configNominalOutputForward(0, RobotMap.TALON_COMMAND_TIMEOUT);
            this.talons[i].configNominalOutputReverse(0, RobotMap.TALON_COMMAND_TIMEOUT);
            this.talons[i].configPeakOutputForward(1, RobotMap.TALON_COMMAND_TIMEOUT);
            this.talons[i].configPeakOutputReverse(-1, RobotMap.TALON_COMMAND_TIMEOUT);
            this.talons[i].configMotionAcceleration(350, RobotMap.TALON_COMMAND_TIMEOUT);
            this.talons[i].configMotionCruiseVelocity(500, RobotMap.TALON_COMMAND_TIMEOUT);

            if (i > 0) {
                this.talons[i].set(ControlMode.Follower, RobotMap.ELEVATOR_TALON_PORTS[0]);
            }
        }

        this.talons[0].overrideLimitSwitchesEnable(true);
        this.talons[0].configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.TALON_COMMAND_TIMEOUT);

        this.talons[0].configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0x00, 0x00, RobotMap.TALON_COMMAND_TIMEOUT);

        this.talons[0].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.TALON_COMMAND_TIMEOUT);

        this.talons[0].config_kP(0, K_P, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].config_kI(0, K_I, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].config_kD(0, K_D, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].config_kF(0, K_F, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].configAllowableClosedloopError(0, ALLOWABLE_ERROR, RobotMap.TALON_COMMAND_TIMEOUT);

        this.zeroed = this.talons[0].getSensorCollection().isRevLimitSwitchClosed();
    }
    public void stop() {
        this.talons[0].set(ControlMode.PercentOutput, 0);
    }

    public void set(ControlMode controlMode, double value) {
        this.talons[0].set(controlMode, value);
    }
    
    public boolean isZeroed() {
        return this.zeroed;
    }

    public void zero() {
        ElevatorZeroCommand zeroCommand = new ElevatorZeroCommand();
        zeroCommand.start();
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new ElevatorControlCommand());
    }
    
    public TalonSRX[] getTalons() {
    	return talons;
    }

}

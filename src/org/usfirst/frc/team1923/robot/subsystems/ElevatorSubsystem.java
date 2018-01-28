package org.usfirst.frc.team1923.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import org.usfirst.frc.team1923.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem {
    
    private TalonSRX[] talons;

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

        this.talons[0].configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.TALON_COMMAND_TIMEOUT);
        this.talons[0].overrideLimitSwitchesEnable(true);
    }

    public void stop() {
        this.talons[0].set(ControlMode.PercentOutput, 0);
    }

    public void set(ControlMode controlMode, double value) {
        this.talons[0].set(controlMode, value);
    }

    @Override
    protected void initDefaultCommand() {

    }

}

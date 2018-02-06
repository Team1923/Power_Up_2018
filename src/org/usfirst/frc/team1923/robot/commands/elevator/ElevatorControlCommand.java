package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Change elevator position based on operator joystick values.
 */
public class ElevatorControlCommand extends Command {
    
    public ElevatorControlCommand() {
        this.requires(Robot.elevatorSubsystem);
    }

    protected void initialize() {

    }

    protected void execute() {
        Robot.elevatorSubsystem.set(ControlMode.PercentOutput, Robot.oi.operator.getRightY());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        Robot.elevatorSubsystem.stop();
    }

    protected void interrupted() {
        this.end();
    }

}

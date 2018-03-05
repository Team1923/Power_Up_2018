package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

/**
 * Change elevator position based on operator joystick values.
 */
public class ElevatorControlCommand extends Command {

    public ElevatorControlCommand() {
        this.requires(Robot.elevatorSubsystem);
    }

    @Override
    protected void execute() {
        double outputPower = Robot.oi.operator.getLeftTrigger() > 0 ? -Robot.oi.operator.getLeftTrigger() : Robot.oi.operator.getRightTrigger();

        outputPower = outputPower * 0.50;

        Robot.elevatorSubsystem.set(ControlMode.PercentOutput, outputPower);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.elevatorSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        Robot.elevatorSubsystem.stop();
    }

}

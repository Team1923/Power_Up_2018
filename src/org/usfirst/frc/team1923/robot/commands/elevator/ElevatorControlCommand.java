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

        outputPower = outputPower * 0.75;

        if (Robot.elevatorSubsystem.getElevatorPosition() < 16 && outputPower < 0) {
            double maxOutputPower = -(0.002 * Math.pow(Robot.elevatorSubsystem.getElevatorPosition(), 2) + 0.175);

            outputPower = Math.max(outputPower, maxOutputPower);
        }

        if (Robot.elevatorSubsystem.getElevatorPosition() > 60 && outputPower > 0) {
            double maxOutputPower = 0.002 * Math.pow(74 - Robot.elevatorSubsystem.getElevatorPosition(), 2) + 0.175;

            outputPower = Math.min(maxOutputPower, outputPower);
        }

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
        this.end();
    }

}

package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

/**
 * Move the elevator to a set position
 */
public class ElevatorPositionCommand extends Command {

    public ElevatorPositionCommand() {
        this.requires(Robot.elevatorSubsystem);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
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

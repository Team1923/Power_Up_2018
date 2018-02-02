package org.usfirst.frc.team1923.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

/**
 * Lower the elevator to starting position and zero the encoders.
 */
public class ElevatorZeroCommand extends Command {

    public ElevatorZeroCommand() {
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

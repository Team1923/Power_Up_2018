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

    protected void initialize() {

    }

    protected void execute() {

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

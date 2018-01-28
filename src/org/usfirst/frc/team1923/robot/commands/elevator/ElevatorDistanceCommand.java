package org.usfirst.frc.team1923.robot.commands.elevator;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * TODO finish and fix
 */
public class ElevatorDistanceCommand extends Command {

	private double distance;
	
    public ElevatorDistanceCommand(double distance) {
    	requires(Robot.elevatorSubsystem);
    	this.distance = distance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// just keep raising elevator by 1 or whatever constant is decided later.
    	Robot.elevatorSubsystem.raise(1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut() || Robot.elevatorSubsystem.getElevatorPosition() >= this.distance;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.elevatorSubsystem.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}

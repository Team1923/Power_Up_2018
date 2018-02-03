package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

public class DriveDistanceCommand extends Command {

	private double left, right;

	public DriveDistanceCommand(double distance) {
		this(distance, distance);

	}

	public DriveDistanceCommand(double left, double right) {
		this.requires(Robot.drivetrainSubsystem);
		this.left = left;
		this.right = right;
	}

	protected void initialize() {

	}

	protected void execute() {

	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.drivetrainSubsystem.stop();
	}

	protected void interrupted() {
		this.end();
	}

}

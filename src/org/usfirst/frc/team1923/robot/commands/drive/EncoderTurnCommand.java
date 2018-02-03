package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.Robot;

public class EncoderTurnCommand extends DriveDistanceCommand {

	/**
	 * Turns a set angle using encoder counts
	 * 
	 * @param angle
	 *            angle to turn right
	 */
	public EncoderTurnCommand(double angle) {
		super(Robot.drivetrainSubsystem.angleToDistance(angle), -Robot.drivetrainSubsystem.angleToDistance(angle));
	}
}

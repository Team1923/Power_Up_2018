package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

public class DriveDistanceCommand extends Command {
	
	private double dist;
    private double target;
	private double left;
	private double right;
	private boolean finished = false;

	public DriveDistanceCommand(double dist) {
		requires(Robot.drivetrainsubsystem);
		this.dist = dist;
		this(0, 0);
	}

	public DriveDistanceCommand(double left, double right) {
		this.requires(Robot.drivetrainSubsystem);
		this.left = left;
		this.right = right;
	}

	public DriveDistanceCommand(double distance) {
		this(distance, distance);
	}

	protected void initialize() {
		Robot.drivetrainSubsystem.configMotionMagic();
        Robot.drivetrainSubsystem.resetPosition();
        
        target = DrivetrainSubsystem.distanceToRotations(this.dist);
        Robot.drivetrainSubsystem.drive(target, target);
	}

	protected void execute() {
        Robot.drivetrainSubsystem.drive(target, target);
		finished = true;
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

	protected void end() {
		Robot.drivetrainSubsystem.stop();
	}

	protected void interrupted() {
		this.end();
	}

}

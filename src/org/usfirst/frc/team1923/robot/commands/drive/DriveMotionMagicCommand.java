package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DriveMotionMagicCommand extends Command {

    private double dist;
    private double target;
    
    public DriveMotionMagicCommand(double dist) {
        this.dist = dist;
        requires(Robot.drivetrainSubsystem);
    }
    
    @Override
    protected void initialize() {
    	Robot.drivetrainSubsystem.configMotionMagic();
        Robot.drivetrainSubsystem.resetPosition();
        
        target = DrivetrainSubsystem.distanceToRotations(this.dist);
        Robot.drivetrainSubsystem.drive(target, target);
    }
    
    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive(target, target);
    }
    
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
        Robot.drivetrainSubsystem.stop();
	}
	
    @Override
    protected void interrupted() {
        this.end();
    }

}

package org.usfirst.frc.team1923.commands.auton;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.commands.drive.*;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOpenCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

// NOT TESTED
public class LeftSwitchAuton extends CommandGroup {

	public LeftSwitchAuton() {
        requires(Robot.drivetrainSubsystem);
        
        addSequential(new DriveTimeCommand(1, 1));
        addSequential(new DriveTimeCommand(0.5, 1.5));
        addSequential(new DriveTimeCommand(0.5, 0.5));
        addSequential(new DriveTimeCommand(1.5, 0.5));
        addSequential(new DriveTimeCommand(1, 1));
        addSequential(new DriveTimeCommand(1.5, 0.5));
        addSequential(new WaitCommand(0.2));
        addSequential(new IntakeOpenCommand());
        addSequential(new DriveTimeCommand(-1, -1));
	}
	
    protected void initialize() {
    }
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}

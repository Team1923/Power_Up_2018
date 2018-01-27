package org.usfirst.frc.team1923.robot.commands.intake;

import org.usfirst.frc.team1923.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class RawIntakeCommand extends Command {
	public RawIntakeCommand() {
        requires(Robot.intakeSubsystem);
    }

    @Override
    public void execute() {
        Robot.intakeSubsystem.pickupItem();
    }

    @Override
    public void end() {
        Robot.intakeSubsystem.stop();
    }

    @Override
    public void interrupted() {
        Robot.intakeSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

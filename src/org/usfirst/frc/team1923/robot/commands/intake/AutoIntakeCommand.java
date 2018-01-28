package org.usfirst.frc.team1923.robot.commands.intake;

import org.usfirst.frc.team1923.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class AutoIntakeCommand extends Command {

    public AutoIntakeCommand() {
        this.requires(Robot.intakeSubsystem);
    }

    @Override
    public void initialize() {
        Robot.intakeSubsystem.intake(0.8);
    }

    @Override
    public void execute() {

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
        // TODO: Return true if operator manual control values are over a limit (ie. 0.4)

        return !Robot.intakeSubsystem.isSecure() || this.isTimedOut();
    }

}

package org.usfirst.frc.team1923.robot.commands.intake;

import org.usfirst.frc.team1923.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Run intake until cube is secured, command times out, or overridden by operator control whichever comes first.
 */
public class AutoIntakeCommand extends Command {

    public AutoIntakeCommand() {
        this(30);
    }

    public AutoIntakeCommand(double timeout) {
        this.requires(Robot.intakeSubsystem);

        this.setTimeout(timeout);
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
        return Robot.oi.operator.getLeftTrigger() > 0.5 || Robot.oi.operator.getRightTrigger() > 0.5 || !Robot.intakeSubsystem.isSecure() || this.isTimedOut();
    }

}

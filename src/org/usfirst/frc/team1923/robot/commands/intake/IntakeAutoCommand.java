package org.usfirst.frc.team1923.robot.commands.intake;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * Run intake until cube is secured, command times out, or overridden by operator control whichever comes first.
 */
public class IntakeAutoCommand extends TimedCommand {

    public IntakeAutoCommand(double timeout) {
        super(timeout);
        this.requires(Robot.intakeSubsystem);
    }

    public IntakeAutoCommand() {
        this(30);
    }

    @Override
    protected void initialize() {
        Robot.intakeSubsystem.intake(0.8);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return Robot.intakeSubsystem.isSecure() || super.isFinished();
    }

    @Override
    protected void end() {
        Robot.intakeSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        Robot.intakeSubsystem.stop();
    }

}

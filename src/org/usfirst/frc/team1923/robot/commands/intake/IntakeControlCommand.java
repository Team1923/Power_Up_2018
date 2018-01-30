package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Set intake wheel speed based on operator control.
 */
public class IntakeControlCommand extends Command {

    public IntakeControlCommand() {
        this.requires(Robot.intakeSubsystem);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Robot.intakeSubsystem.intakeLeft(Robot.oi.operator.getLeftTrigger());
        Robot.intakeSubsystem.intakeRight(Robot.oi.operator.getRightTrigger());
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

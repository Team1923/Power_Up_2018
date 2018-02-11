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
    protected void execute() {
        Robot.intakeSubsystem.intakeLeft(Robot.oi.operator.leftTrigger.getX());
        Robot.intakeSubsystem.intakeRight(Robot.oi.operator.rightTrigger.getX());
    }

    @Override
    protected boolean isFinished() {
        return false;
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

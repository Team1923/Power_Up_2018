package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.utils.logger.TransientDataSource;

/**
 * Set intake wheel speed based on operator control.
 */
public class IntakeControlCommand extends Command {

    public IntakeControlCommand() {
        this.requires(Robot.intakeSubsystem);

//        Robot.logger.addTransientDataSource("IntakeControlCommand_LeftOutput", new TransientDataSource(
//                () -> Robot.oi.operator.getLeftY() + "",
//                this::isRunning
//        ));
//
//        Robot.logger.addTransientDataSource("IntakeControlCommand_RightOutput", new TransientDataSource(
//                () -> Robot.oi.operator.getRightY() + "",
//                this::isRunning
//        ));
    }

    @Override
    protected void execute() {
        Robot.intakeSubsystem.intakeLeft(Robot.oi.operator.getLeftY());
        Robot.intakeSubsystem.intakeRight(Robot.oi.operator.getRightY());
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

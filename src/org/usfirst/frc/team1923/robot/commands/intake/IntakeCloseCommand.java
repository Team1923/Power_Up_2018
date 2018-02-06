package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Close the intake.
 */
public class IntakeCloseCommand extends InstantCommand {

    public IntakeCloseCommand() {
        this.requires(Robot.intakeSubsystem);
    }

    @Override
    public void initialize() {
        Robot.intakeSubsystem.close();
    }

}

package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Open the intake.
 */
public class IntakeOpenCommand extends InstantCommand {

    public IntakeOpenCommand() {
        this.requires(Robot.intakeSubsystem);
    }

    @Override
    public void initialize() {
        Robot.intakeSubsystem.open();
    }

}

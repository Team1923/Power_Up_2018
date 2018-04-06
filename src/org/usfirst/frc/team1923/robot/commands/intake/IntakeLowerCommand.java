package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.usfirst.frc.team1923.robot.Robot;

public class IntakeLowerCommand extends InstantCommand {

    public IntakeLowerCommand() {
        // this.requires(Robot.intakeSubsystem);
    }

    @Override
    protected void initialize() {
        Robot.intakeSubsystem.lower();
    }

}

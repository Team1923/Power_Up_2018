package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.usfirst.frc.team1923.robot.Robot;

public class IntakeToggleCommand extends InstantCommand {

    private static IntakeToggleCommand instance;

    private boolean toggled;

    private IntakeToggleCommand() {
        this.requires(Robot.intakeSubsystem);

        this.toggled = true;
    }

    @Override
    protected void initialize() {
        System.out.println(this.toggled);

        if (this.toggled) {
            Robot.intakeSubsystem.close();
        } else {
            Robot.intakeSubsystem.open();
        }

        this.toggled = !this.toggled;
    }

    public static IntakeToggleCommand getInstance() {
        if (instance == null) {
            instance = new IntakeToggleCommand();
        }

        return instance;
    }

}
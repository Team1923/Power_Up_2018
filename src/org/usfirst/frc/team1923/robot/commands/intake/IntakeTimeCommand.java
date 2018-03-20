package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.TimedCommand;

import org.usfirst.frc.team1923.robot.Robot;

public class IntakeTimeCommand extends TimedCommand {

    private double power;

    public IntakeTimeCommand(double power) {
        this(power, 1);
    }

    public IntakeTimeCommand(double power, double timeout) {
        super(timeout);
        this.requires(Robot.intakeSubsystem);

        this.power = power;
    }

    @Override
    protected void initialize() {
        Robot.intakeSubsystem.intake(this.power);
    }

    @Override
    protected void end() {
        Robot.intakeSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

}

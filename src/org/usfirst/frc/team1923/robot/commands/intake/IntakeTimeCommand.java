package org.usfirst.frc.team1923.robot.commands.intake;

import edu.wpi.first.wpilibj.command.TimedCommand;

import org.usfirst.frc.team1923.robot.Robot;

public class IntakeOutputCommand extends TimedCommand {

    private double power;

    public IntakeOutputCommand(double power) {
        super(1);
        this.requires(Robot.intakeSubsystem);

        this.power = power;
    }

    @Override
    protected void initialize() {
        Robot.intakeSubsystem.intake(-this.power);

        System.out.println("IntakeOutputCommand Init @ " + System.currentTimeMillis());
    }

    @Override
    protected void end() {
        Robot.intakeSubsystem.stop();

        System.out.println("IntakeOutputCommand End @ " + System.currentTimeMillis());
    }

    @Override
    protected void interrupted() {
        Robot.intakeSubsystem.stop();
    }

}
package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.Robot;

@Autonomous
public class DoNothingAuton extends Command {

    public DoNothingAuton() {
        this.requires(Robot.drivetrainSubsystem);
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        Robot.drivetrainSubsystem.stop();
    }

}

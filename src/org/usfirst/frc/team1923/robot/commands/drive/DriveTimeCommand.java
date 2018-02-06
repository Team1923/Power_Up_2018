package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Drive the robot forward, in a straight line, for a specified amount of time (in seconds).
 */
public class DriveTimeCommand extends Command {

    private double power;

    public DriveTimeCommand(double power, double timeout) {
        this.requires(Robot.drivetrainSubsystem);
        this.setTimeout(timeout);

        this.power = power;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive(this.power, this.power);
    }

    @Override
    protected boolean isFinished() {
        return this.isTimedOut();
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

}

package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.TimedCommand;

import org.usfirst.frc.team1923.robot.Robot;

/**
 * Drive the robot forward, in a straight line, for a specified amount of time (in seconds).
 */
public class DriveTimeCommand extends TimedCommand {

    private double power;

    public DriveTimeCommand(double power, double timeout) {
        super(timeout);
        this.requires(Robot.drivetrainSubsystem);

        this.power = power;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.drive(this.power, this.power);
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

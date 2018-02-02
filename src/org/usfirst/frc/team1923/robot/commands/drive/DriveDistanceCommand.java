package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Drive the robot forward, in a straight line, for a distance (in inches).
 */
public class DriveDistanceCommand extends Command {

    private double power;
    private double distance;

    public DriveDistanceCommand(double power, double distance) {
        this.requires(Robot.drivetrainSubsystem);

        this.power = power;
        this.distance = distance;
    }

    @Override
    protected void initialize() {
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

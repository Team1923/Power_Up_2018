package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Drive the robot for a distance (in inches) using TalonSRX Motion Magic.
 */
public class DriveDistanceCommand extends Command {

    private double leftDistance;
    private double rightDistance;

    public DriveDistanceCommand(double distance) {
        this(distance, distance);
    }

    public DriveDistanceCommand(double leftDistance, double rightDistance) {
        this.requires(Robot.drivetrainSubsystem);

        this.leftDistance = leftDistance;
        this.rightDistance = rightDistance;
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

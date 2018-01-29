package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

public class DriveDistanceCommand extends Command {

    public DriveDistanceCommand() {
        this.requires(Robot.drivetrainSubsystem);
    }

    protected void initialize() {

    }

    protected void execute() {

    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    protected void interrupted() {
        this.end();
    }

}

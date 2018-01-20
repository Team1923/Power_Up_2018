package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class RawDriveCommand extends Command {

    public RawDriveCommand() {
        requires(Robot.driveSubSys);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        // Robot.driveSubSys.drive(Robot.oi.driver.getLeftY(),
        // Robot.oi.driver.getRightY());
        // commented because oi isnt done yet
    }

    @Override
    public void end() {
        Robot.driveSubSys.stop();
    }

    @Override
    public void interrupted() {
        Robot.driveSubSys.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

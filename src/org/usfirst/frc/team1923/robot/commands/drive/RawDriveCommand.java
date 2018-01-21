package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

public class RawDriveCommand extends Command {

    public RawDriveCommand() {
        requires(Robot.driveSubSys);
    }

    @Override
    public void initialize() {

    }
    
    public void execute() {
        Robot.driveSubSys.drive(Robot.oi.driver.getLeftY(), Robot.oi.driver.getRightY());
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

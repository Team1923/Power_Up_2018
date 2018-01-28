package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;    

import org.usfirst.frc.team1923.robot.Robot;

public class RawDriveCommand extends Command {

    public RawDriveCommand() {
        requires(Robot.drivetrainSubsystem);
    }

    @Override
    public void execute() {
        Robot.drivetrainSubsystem.drive(Robot.oi.driver.getLeftY(), Robot.oi.driver.getRightY());
    }

    @Override
    public void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    public void interrupted() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

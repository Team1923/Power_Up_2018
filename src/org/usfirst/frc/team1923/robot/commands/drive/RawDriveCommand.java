package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

/**
 * Drive the robot based on driver joystick values.
 */
public class RawDriveCommand extends Command {

    public RawDriveCommand() {
        this.requires(Robot.drivetrainSubsystem);
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

package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive the robot based on driver joystick values.
 */
public class DriveControlCommand extends Command {

    public DriveControlCommand() {
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

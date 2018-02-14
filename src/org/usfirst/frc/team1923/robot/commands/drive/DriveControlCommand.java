package org.usfirst.frc.team1923.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

/**
 * Drive the robot based on driver joystick values.
 */
public class DriveControlCommand extends Command {

    public DriveControlCommand() {
        this.requires(Robot.drivetrainSubsystem);
    }
    
    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive(Robot.oi.driver.getLeftY(), Robot.oi.driver.getRightY());
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

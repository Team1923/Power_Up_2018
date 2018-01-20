package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTimeCommand extends Command {

    private double power;

    /**
     * Drives a set time at a set speed
     * 
     * @param power
     * @param timeOut
     *            Timeout in seconds
     */
    public DriveTimeCommand(double power, double timeOut) {
        requires(Robot.driveSubSys);
        setTimeout(timeOut);
        this.power = power;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.driveSubSys.drive(this.power, this.power);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        Robot.driveSubSys.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }

}

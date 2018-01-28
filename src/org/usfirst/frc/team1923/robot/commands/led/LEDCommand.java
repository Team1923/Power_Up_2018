package org.usfirst.frc.team1923.robot.commands.led;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;

/**
 * Test command to output LED profiles to Arduino.
 */
public class LEDCommand extends Command {

    private long lastRun;

    public LEDCommand() {
        this.requires(Robot.ledSubsystem);

        this.lastRun = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        if (this.lastRun + 90 > System.currentTimeMillis()) {
            return;
        }

        Robot.ledSubsystem.getArduino().write(Robot.ledSubsystem.getProfile().getNext(), 180);
        Robot.ledSubsystem.getArduino().flush();
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        this.end();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

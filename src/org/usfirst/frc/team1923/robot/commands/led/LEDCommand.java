package org.usfirst.frc.team1923.robot.commands.led;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem.LEDMode;

/**
 * Test command to output LED profiles to Arduino.
 */
public class LEDCommand extends Command {

    public LEDCommand() {
        this.requires(Robot.ledSubsystem);
    }

    @Override
    public void execute() {
        Robot.ledSubsystem.setMode(Robot.oi.driver.square.get() ? LEDMode.ON : LEDMode.OFF);
    }

    @Override
    public void end() {
        Robot.ledSubsystem.setMode(LEDMode.OFF);
    }

    @Override
    public void interrupted() {
        Robot.ledSubsystem.setMode(LEDMode.OFF);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

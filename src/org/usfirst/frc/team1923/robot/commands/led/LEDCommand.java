package org.usfirst.frc.team1923.robot.commands.led;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;

/**
 * Test command to output LED profiles to Arduino.
 */
public class LEDCommand extends Command {

    public LEDCommand() {
        this.requires(Robot.ledSubsystem);
    }

    @Override
    public void execute() {
        boolean newPressed = Robot.oi.driver.square.get();
        if (newPressed == (Robot.ledSubsystem.getCurrentMode() == LEDSubsystem.Mode.OFF)) {
            Robot.ledSubsystem.setMode(newPressed ? LEDSubsystem.Mode.ON : LEDSubsystem.Mode.OFF);
        }
    }

    @Override
    public void end() {
        Robot.ledSubsystem.setMode(LEDSubsystem.Mode.OFF);
    }

    @Override
    public void interrupted() {
        Robot.ledSubsystem.setMode(LEDSubsystem.Mode.OFF);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

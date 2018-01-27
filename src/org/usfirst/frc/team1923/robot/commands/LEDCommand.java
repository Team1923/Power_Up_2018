package org.usfirst.frc.team1923.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;

import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;

public class LEDCommand extends Command {

    public LEDCommand() {
        requires(Robot.ledSubsystem);
    }

    @Override
    public void execute() {
        boolean newPressed = Robot.oi.driver.square.get();
        if (newPressed == (Robot.ledSubsystem.currentMode == LEDSubsystem.Mode.Off)) {
            Robot.ledSubsystem.setMode(newPressed ? LEDSubsystem.Mode.On : LEDSubsystem.Mode.Off);
        }
    }

    @Override
    public void end() {
        Robot.ledSubsystem.setMode(LEDSubsystem.Mode.Off);
    }

    @Override
    public void interrupted() {
        Robot.ledSubsystem.setMode(LEDSubsystem.Mode.Off);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

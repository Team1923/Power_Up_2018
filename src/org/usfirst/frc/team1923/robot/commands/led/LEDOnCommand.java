package org.usfirst.frc.team1923.robot.commands.led;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem.LEDMode;

public class LEDOnCommand extends Command {

    public LEDOnCommand() {
        this.requires(Robot.ledSubsystem);
    }

    @Override
    protected void initialize() {
        Robot.ledSubsystem.setMode(LEDMode.ON);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.ledSubsystem.setMode(LEDMode.OFF);
    }

    @Override
    protected void interrupted() {
        Robot.ledSubsystem.setMode(LEDMode.OFF);
    }

}

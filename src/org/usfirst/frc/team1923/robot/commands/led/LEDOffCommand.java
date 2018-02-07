package org.usfirst.frc.team1923.robot.commands.led;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem.LEDMode;

public class LEDOffCommand extends Command {

    public LEDOffCommand() {
        this.requires(Robot.ledSubsystem);
    }

    @Override
    protected void initialize() {
        Robot.ledSubsystem.setMode(LEDMode.OFF);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}

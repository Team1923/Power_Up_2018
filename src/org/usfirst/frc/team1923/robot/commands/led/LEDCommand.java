package org.usfirst.frc.team1923.robot.commands.led;

import org.usfirst.frc.team1923.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.subsytems.LEDSubsytem;

public class LEDCommand extends Command {

    public LEDCommand() {
        this.requires(Robot.ledSubsystem);
    }

    @Override
    public void execute() {
        //Something in color
        Robot.ledSubsystem.setColor();
        //Something in Pattern
        Robot.ledSubsystem.setPattern();
    }

    @Override
    public void end() {
        Robot.ledSubsystem.setPattern(0);
    }

    @Override
    public void interrupted() {
        Robot.ledSubsystem.setPattern(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}

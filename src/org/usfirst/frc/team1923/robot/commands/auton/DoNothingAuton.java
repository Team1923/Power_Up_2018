package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

public class DoNothingAuton extends Command implements AutonCommand {

    public DoNothingAuton() {
        this.requires(Robot.drivetrainSubsystem);
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void execute() {

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

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return true;
    }

}

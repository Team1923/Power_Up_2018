package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;

@Autonomous(
        name =  "Do Nothing",
        startingPosition = { Autonomous.Side.LEFT, Autonomous.Side.CENTER, Autonomous.Side.RIGHT, Autonomous.Side.NONE },
        defaultPriority = 1
)
public class DoNothingAuton extends Command {

    @Override
    protected boolean isFinished() {
        return false;
    }

}

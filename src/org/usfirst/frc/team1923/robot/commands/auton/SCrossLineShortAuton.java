package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

@Autonomous(
        name = "Cross the Line Short",
        description = "Drive forward 150 inches",
        startingPosition = { Autonomous.Side.LEFT_STRAIGHT, Autonomous.Side.RIGHT_STRAIGHT },
        defaultPriority = 25
)
public class SCrossLineShortAuton extends CommandGroup {

    public SCrossLineShortAuton() {
        this.addSequential(new DriveDistanceCommand(150));
    }

}

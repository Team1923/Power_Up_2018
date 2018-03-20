package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

@Autonomous(
        name = "Cross the Line",
        startingPosition = { Autonomous.Side.LEFT, Autonomous.Side.RIGHT },
        defaultPriority = 25
)
public class CrossLineAuton extends CommandGroup {

    public CrossLineAuton() {
        this.addSequential(new DriveDistanceCommand(200));
    }

}

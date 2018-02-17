package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

@Autonomous(
        name = "Cross the Line Short",
        description = "Drive forward 150 inches",
        defaultPriority = 25
)
public class CrossLineShortAuton extends CommandGroup {

    public CrossLineShortAuton() {
        this.addSequential(new DriveDistanceCommand(150));
    }

}

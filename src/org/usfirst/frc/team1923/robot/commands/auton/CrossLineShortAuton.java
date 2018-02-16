package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

@Autonomous
public class CrossLineShortAuton extends CommandGroup {

    public CrossLineShortAuton() {
        this.addSequential(new DriveDistanceCommand(150));
    }

}

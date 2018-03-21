package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;

@Autonomous(
        name = "L > Park Center",
        startingPosition = Autonomous.Side.LEFT,
        defaultPriority = 25
)
public class LeftParkCenterAuton extends CommandGroup {

    public LeftParkCenterAuton() {
        this.addSequential(new DriveDistanceCommand(212));
        this.addSequential(new TurnEncoderCommand(-105));
        this.addSequential(new DriveDistanceCommand(80));
    }

}

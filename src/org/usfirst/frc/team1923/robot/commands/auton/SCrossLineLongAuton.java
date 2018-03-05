package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;

@Autonomous(
        name = "Cross the Line Long",
        description = "Drive forward 280 inches",
        startingPosition = { Autonomous.Side.LEFT_STRAIGHT, Autonomous.Side.RIGHT_STRAIGHT },
        defaultPriority = 25
)
public class SCrossLineLongAuton extends CommandGroup {

    public SCrossLineLongAuton() {
        this.addSequential(new DriveDistanceCommand(280));
    }

}

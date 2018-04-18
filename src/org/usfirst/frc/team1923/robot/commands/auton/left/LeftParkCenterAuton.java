package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "L > Park Center",
        startingPosition = Autonomous.Side.LEFT,
        defaultPriority = 25
)
public class LeftParkCenterAuton extends CommandGroup {

    public LeftParkCenterAuton() {
        this.addSequential(new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_PARKCENTER));
    }

}

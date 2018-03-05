package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "Left Left-Scale",
        description = "Starting from the left, place a cube in the left scale",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class LeftLScaleAuton extends CommandGroup {

    public LeftLScaleAuton() {
        this.addSequential(new DriveTrajectoryCommand(TrajectoryStore.Waypoints.LEFT_LSWITCHLAYUP));
    }

}

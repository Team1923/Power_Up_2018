package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "Right Right-Scale",
        description = "Starting from the right, place a cube in the right scale",
        startingPosition = Autonomous.Side.RIGHT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 50
)
public class RightRScaleAuton extends CommandGroup {

    public RightRScaleAuton() {
        this.addSequential(new DriveTrajectoryCommand(TrajectoryStore.Waypoints.RIGHT_RSCALE));
    }

}

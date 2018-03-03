package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "Center Right-Scale",
        description = "Starting from the center, place a cube in the right scale",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 50
)
public class CenterRScaleAuton extends CommandGroup {

    public CenterRScaleAuton() {
        this.addParallel(new DriveTrajectoryCommand(TrajectoryStore.Waypoints.CENTER_RSCALE));
        this.addSequential(new QueueCommand(
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                () -> false
        ));
        this.addSequential(new IntakeLowerCommand());
        this.addSequential(new IntakeOutputCommand(-1.0));
    }

}

package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "Center Left-Switch",
        description = "Starting from the center, place a cube in the left switch",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 75
)
public class CenterLSwitchAuton extends CommandGroup {

    public CenterLSwitchAuton() {
        CommandGroup commandGroup = new CommandGroup();
        commandGroup.addParallel(new DriveTrajectoryCommand(TrajectoryStore.Waypoints.CENTER_LSWITCHLAYUP));
        commandGroup.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));

        this.addSequential(commandGroup);
        this.addSequential(new IntakeOutputCommand(-1.0));
    }

}

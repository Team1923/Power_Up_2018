package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "Center Right-Switch",
        description = "Starting from the center, place a cube in the right switch",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 75
)
public class CenterRSwitchAuton extends CommandGroup {

    public CenterRSwitchAuton() {
        CommandGroup commandGroup = new CommandGroup();
        commandGroup.addParallel(new DriveTrajectoryCommand(TrajectoryStore.Waypoints.CENTER_RSWITCHLAYUP));
        commandGroup.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));

        this.addSequential(commandGroup);
        this.addSequential(new IntakeOutputCommand(-1.0));

//        CommandGroup commandGroup = new CommandGroup();
//        DriveTrajectoryCommand drive = new DriveTrajectoryCommand(TrajectoryStore.Waypoints.CENTER_RSWITCHLAYUP);
//        commandGroup.addParallel(drive);
//        commandGroup.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
//
//        commandGroup.addParallel(new QueueCommand(
//                new IntakeOutputCommand(-1.0),
//                () -> drive.getLeftSegmentId() > 145 && drive.getRightSegmentId() > 145
//        ));
    }

}

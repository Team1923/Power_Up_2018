package org.usfirst.frc.team1923.robot.commands.auton.center;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "C > LSwitch",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 75
)
public class CenterLSwitchAuton extends CommandGroup {

    public CenterLSwitchAuton() {
        DriveTrajectoryCommand drive = new DriveTrajectoryCommand(TrajectoryStore.Path.CENTER_LSWITCHLAYUP);

        this.addSequential(CGUtils.parallel(
                drive,
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH),
                new QueueCommand(
                        new IntakeTimeCommand(0.5),
                        () -> drive.isAlmostFinished(50)
                )
        ));
    }

}

package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorTimeCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "L > LScale > LSwitch",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL },
        defaultPriority = 50
)
public class LeftLScaleLSwitchAuton extends CommandGroup {

    public LeftLScaleLSwitchAuton() {
        DriveTrajectoryCommand drive = new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_LSCALE);

        this.addSequential(CGUtils.parallel(
                drive,
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(0.75),
                        new IntakeRaiseCommand()
                ),
                new QueueCommand(
                        new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                        () -> drive.isAlmostFinished(75)
                )
        ));
        this.addSequential(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP));
        this.addSequential(new DriveDistanceCommand(8));
        this.addSequential(new IntakeTimeCommand(0.50, 0.5));
        this.addSequential(new DriveDistanceCommand(-12.0));
        this.addSequential(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));
        this.addSequential(CGUtils.parallel(
                //new TurnEncoderCommand(-155),
                new IntakeLowerCommand(),
                new ElevatorTimeCommand(-0.1, 0.8)
        ));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(44),
                new IntakeTimeCommand(-1.0, 2.25)
        ));
        this.addSequential(new DriveDistanceCommand(-6.0));
        this.addSequential(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(12),
                new IntakeTimeCommand(1.0)
        ));
    }

}

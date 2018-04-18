package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;
import org.usfirst.frc.team1923.robot.utils.pathfinder.modifier.DistancePathModifier;
import org.usfirst.frc.team1923.robot.utils.pathfinder.modifier.TrimPathModifier;

@Autonomous(
        name = "L > RScale",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 50
)
public class LeftRScaleAuton extends CommandGroup {

    public LeftRScaleAuton() {
        DriveDistanceCommand drive = new DriveDistanceCommand(24, 24, 36, 36, Integer.MAX_VALUE);
        DriveDistanceCommand drive2 = new DriveDistanceCommand(24, 24, 36, 36, Integer.MAX_VALUE);
        DriveDistanceCommand driveBack = new DriveDistanceCommand(-24, -24, 36, 36, Integer.MAX_VALUE);
        DriveDistanceCommand driveBack2 = new DriveDistanceCommand(-30, -30, -96, -108, Integer.MAX_VALUE);
        this.addSequential(CGUtils.parallel(
                new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_RSCALE)
                        .modify(new TrimPathModifier(25)),
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(1.0),
                        new IntakeRaiseCommand()
                )
        ));
        this.addSequential(new TurnEncoderCommand(160).setVelocity(84).setAcceleration(56));
        this.addSequential(CGUtils.parallel(
                drive,
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                new QueueCommand(
                        new IntakeTimeCommand(0.4, 0.4),
                        () -> drive.isAlmostFinished(3000)
                )
        ));
        this.addSequential(CGUtils.parallel(
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM),
                driveBack,
                new QueueCommand(
                        new IntakeLowerCommand(),
                        () -> driveBack.isAlmostFinished(2500)
                )
        ));
        this.addSequential(new TurnEncoderCommand(205));
        this.addSequential(CGUtils.parallel(
                new IntakeTimeCommand(-0.5, 1.5),
                new DriveDistanceCommand(30, 30, 72, 72, Integer.MAX_VALUE)
        ));
        this.addSequential(CGUtils.parallel(
                driveBack2,
                new IntakeTimeCommand(-0.5, 0.5),
                new QueueCommand(
                        new IntakeRaiseCommand(),
                        () -> driveBack2.isAlmostFinished(3000)
                )
        ));
        this.addSequential(new TurnEncoderCommand(-165).setVelocity(84).setAcceleration(56));
        this.addSequential(CGUtils.parallel(
                drive2,
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                new QueueCommand(
                        new IntakeTimeCommand(0.6, 0.45),
                        () -> drive2.isAlmostFinished(3000)
                )
        ));
    }

}

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
        this.addSequential(CGUtils.parallel(
                new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_RSCALE)
                        .modify(new TrimPathModifier(25)),
                        // .modifyRight(new DistancePathModifier(120, 0, 60, 0).setMatchVelocity(true)),
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(1.0),
                        new IntakeRaiseCommand()
                )
        ));
        this.addSequential(new TurnEncoderCommand(160));
        this.addSequential(CGUtils.parallel(
                drive,
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                new QueueCommand(
                        new IntakeTimeCommand(0.4, 0.4),
                        () -> drive.isAlmostFinished(3000)
                )
        ));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(-30, -30, 36, 36, Integer.MAX_VALUE),
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM)
        ));
        this.addSequential(new TurnEncoderCommand(90));



//        this.addSequential(new TurnEncoderCommand(160));
//        this.addSequential(CGUtils.parallel(
//                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
//                new DriveDistanceCommand(18, 18, 24, 24, Integer.MAX_VALUE)
//        ));
//        this.addSequential(new IntakeTimeCommand(0.65, 0.45));
//        this.addSequential(new DriveDistanceCommand(-18, -18, 24, 24, Integer.MAX_VALUE));
//        this.addSequential(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));




        // OLD STUFF PRE-LEHIGH
//        this.addSequential(CGUtils.parallel(
//                new DriveDistanceCommand(208, 84, 96, Integer.MAX_VALUE),
//                CGUtils.sequential(
//                        new IntakeLowerCommand(),
//                        new WaitCommand(0.75),
//                        new IntakeRaiseCommand()
//                )
//        ));
//      //  this.addSequential(new TurnEncoderCommand(-115));
//        this.addSequential(new DriveDistanceCommand(204, 84, 96, Integer.MAX_VALUE));
//      //  this.addSequential(new TurnEncoderCommand(140));
//        this.addSequential(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP));
//        this.addSequential(new DriveDistanceCommand(40));
//        this.addSequential(new IntakeTimeCommand(0.50));
//        this.addSequential(CGUtils.parallel(
//                new DriveDistanceCommand(-20),
//                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM)
//        ));
//    //    this.addSequential(new TurnEncoderCommand(-180));



        // OLD STUFF PRE-LEHIGH
//        this.addSequential(new TurnGyroCommand(120));
//        this.addSequential(CGUtils.parallel(
//                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
//                drive,
//                new QueueCommand(
//                        new IntakeTimeCommand(0.45, 0.45),
//                        () -> drive.isAlmostFinished(500)
//                )
//        ));
//        this.addSequential(CGUtils.parallel(
//                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM),
//                new DriveDistanceCommand(-12, -12, 72, 72, Integer.MAX_VALUE)
//        ));
//        this.addSequential(new TurnGyroCommand(110));
    }

}

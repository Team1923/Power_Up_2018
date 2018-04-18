package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnGyroCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;
import org.usfirst.frc.team1923.robot.utils.pathfinder.modifier.TrimPathModifier;

@Autonomous(
        name = "L > LScale (x2)",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class LeftLScaleAuton extends CommandGroup {

    public LeftLScaleAuton() {
        final DriveTrajectoryCommand drive = new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_LSCALE).modify(new TrimPathModifier(5));
        final DriveDistanceCommand driveReverse = new DriveDistanceCommand(-20, -20, 84, 84, Integer.MAX_VALUE);
        final ElevatorPositionCommand elevator = new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP);

        this.addSequential(CGUtils.parallel(
                drive,
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(1.0),
                        new IntakeRaiseCommand()
                ),
                new QueueCommand(
                        elevator,
                        () -> drive.isAlmostFinished(80)
                ),
                new QueueCommand(
                        new IntakeTimeCommand(0.65, 0.40),
                        () -> drive.isAlmostFinished(25) && elevator.isAlmostFinished(5)
                )
        ));
        this.addSequential(CGUtils.parallel(
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM),
                driveReverse,
                new QueueCommand(
                        new IntakeLowerCommand(),
                        () -> driveReverse.isAlmostFinished(Converter.inchesToTicks(4, RobotMap.Drivetrain.WHEEL_DIAMETER))
                )
        ));
        this.addSequential(new TurnGyroCommand(-105));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(43, 43, 84, 84, Integer.MAX_VALUE),
                new IntakeTimeCommand(-0.50, 2.0)
        ));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(-43, -43, 118, 118, Integer.MAX_VALUE),
                CGUtils.sequential(
                        new IntakeTimeCommand(-0.5, 1.0),
                        new IntakeRaiseCommand()
                )
        ));
        this.addSequential(new TurnEncoderCommand(145));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(32, 32, 36, 36, Integer.MAX_VALUE),
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP)
        ));
        this.addSequential(new IntakeTimeCommand(0.60, 0.40));
    }

}

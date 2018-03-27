package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "L > LScale (x2)",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class LeftLScaleAuton extends CommandGroup {

    public LeftLScaleAuton() {
        final DriveTrajectoryCommand drive = new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_LSCALE);
        final DriveDistanceCommand driveReverse = new DriveDistanceCommand(-20, 72, 72, Integer.MAX_VALUE);
        final ElevatorPositionCommand elevator = new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP);

        this.addSequential(CGUtils.parallel(
                drive,
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(0.75),
                        new IntakeRaiseCommand()
                ),
                new QueueCommand(
                        elevator,
                        () -> drive.isAlmostFinished(100)
                ),
                new QueueCommand(
                        new IntakeTimeCommand(0.5, 0.45),
                        () -> drive.isAlmostFinished(40) && elevator.isAlmostFinished(1.5)
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
        // this.addSequential(new TurnEncoderCommand(-75));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(50, 84, 84, Integer.MAX_VALUE),
                new IntakeTimeCommand(-0.70, 2.5)
        ));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(-50, 84, 84, Integer.MAX_VALUE),
                CGUtils.sequential(
                        new WaitCommand(0.5),
                        new IntakeRaiseCommand()
                )
        ));
        // this.addSequential(new TurnEncoderCommand(70));
        this.addSequential(CGUtils.parallel(
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                new DriveDistanceCommand(20)
        ));
        this.addSequential(new IntakeTimeCommand(0.5, 0.5));
    }

}

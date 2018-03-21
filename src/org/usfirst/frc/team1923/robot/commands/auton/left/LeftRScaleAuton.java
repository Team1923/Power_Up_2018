package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;

@Autonomous(
        name = "L > RScale",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 50
)
public class LeftRScaleAuton extends CommandGroup {

    public LeftRScaleAuton() {
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(208, 208, 84, 96, Integer.MAX_VALUE),
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(0.75),
                        new IntakeRaiseCommand()
                )
        ));
        this.addSequential(new TurnEncoderCommand(-115));
        this.addSequential(new DriveDistanceCommand(204, 216, 84, 96, Integer.MAX_VALUE));
        this.addSequential(new TurnEncoderCommand(140));
        this.addSequential(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP));
        this.addSequential(new DriveDistanceCommand(40));
        this.addSequential(new IntakeTimeCommand(0.50));
        this.addSequential(CGUtils.parallel(
                new DriveDistanceCommand(-20),
                new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM)
        ));
        this.addSequential(new TurnEncoderCommand(-180));
    }

}

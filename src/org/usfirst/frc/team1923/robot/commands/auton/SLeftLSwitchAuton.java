package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;

@Autonomous(
        name = "Left Left-Switch",
        description = "Starting from the left, place a cube in the left switch",
        startingPosition = Autonomous.Side.LEFT_STRAIGHT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 75
)
public class SLeftLSwitchAuton extends CommandGroup {

    public SLeftLSwitchAuton() {
        this.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.addSequential(new DriveDistanceCommand(140));
        this.addSequential(new TurnEncoderCommand(-95));
        this.addSequential(new DriveDistanceCommand(18));
        this.addSequential(new IntakeOutputCommand(-1.0));
    }

}

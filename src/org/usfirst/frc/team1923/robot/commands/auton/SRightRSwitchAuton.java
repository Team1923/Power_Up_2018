package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;

@Autonomous(
        name = "Right Right-Switch",
        description = "Starting from the right, place a cube in the right switch",
        startingPosition = Autonomous.Side.RIGHT_STRAIGHT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 75
)
public class SRightRSwitchAuton extends CommandGroup {

    public SRightRSwitchAuton() {
        this.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.addSequential(new DriveDistanceCommand(140));
        this.addSequential(new TurnEncoderCommand(95));
        this.addSequential(new DriveDistanceCommand(18));
        this.addSequential(new IntakeOutputCommand(-1.0));
    }

}

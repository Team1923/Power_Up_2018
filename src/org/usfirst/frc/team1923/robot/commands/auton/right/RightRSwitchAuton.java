package org.usfirst.frc.team1923.robot.commands.auton.right;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;

@Autonomous(
        name = "R > RSwitch",
        startingPosition = Autonomous.Side.RIGHT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 75
)
public class RightRSwitchAuton extends CommandGroup {

    public RightRSwitchAuton() {
        this.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.addSequential(new DriveDistanceCommand(140));
        this.addSequential(new TurnEncoderCommand(95));
        this.addSequential(new DriveDistanceCommand(18));
        this.addSequential(new IntakeTimeCommand(1.0));
    }

}

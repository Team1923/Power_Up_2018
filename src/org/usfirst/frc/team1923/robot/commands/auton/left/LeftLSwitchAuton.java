package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;

@Autonomous(
        name = "L > LSwitch",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 75
)
public class LeftLSwitchAuton extends CommandGroup {

    public LeftLSwitchAuton() {
        this.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.addSequential(new DriveDistanceCommand(140));
        this.addSequential(new TurnEncoderCommand(-95));
        this.addSequential(new DriveDistanceCommand(18));
        this.addSequential(new IntakeTimeCommand(1.0));

    }

}
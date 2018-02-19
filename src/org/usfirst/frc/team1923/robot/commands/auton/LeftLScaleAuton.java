package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;

@Autonomous(
        name = "Left Left-Scale",
        description = "Starting from the left, place a cube in the left scale",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class LeftLScaleAuton extends CommandGroup {

    public LeftLScaleAuton() {
        this.addParallel(new DriveDistanceCommand(140));
        this.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));
        this.addSequential(new TurnEncoderCommand(-90));
        this.addSequential(new IntakeOutputCommand(0.5));
    }

}

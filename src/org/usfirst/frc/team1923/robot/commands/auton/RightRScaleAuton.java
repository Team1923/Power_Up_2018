package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnEncoderCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOutputCommand;

@Autonomous(
        name = "Right Right-Scale",
        description = "Starting from the right, place a cube in the right scale",
        startingPosition = Autonomous.Side.RIGHT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 50
)
public class RightRScaleAuton extends CommandGroup {

    public RightRScaleAuton() {
        this.addParallel(new DriveDistanceCommand(140));
        this.addParallel(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));
        this.addSequential(new TurnEncoderCommand(90));
        this.addSequential(new IntakeOutputCommand(0.5));
    }

}

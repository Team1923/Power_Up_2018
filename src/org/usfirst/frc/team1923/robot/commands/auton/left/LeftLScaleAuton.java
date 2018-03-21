package org.usfirst.frc.team1923.robot.commands.auton.left;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.commands.QueueCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.CGUtils;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

@Autonomous(
        name = "L > LScale",
        startingPosition = Autonomous.Side.LEFT,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class LeftLScaleAuton extends CommandGroup {

    public LeftLScaleAuton() {
        DriveTrajectoryCommand drive = new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_LSCALE);

        this.addSequential(CGUtils.parallel(
                drive,
                CGUtils.sequential(
                        new IntakeLowerCommand(),
                        new WaitCommand(0.75),
                        new IntakeRaiseCommand()
                ),
                new QueueCommand(
                        new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP),
                        () -> drive.isAlmostFinished(100)
                )
        ));
        this.addSequential(new IntakeTimeCommand(0.5, 0.5));

    }

}

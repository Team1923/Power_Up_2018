package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;

@Autonomous(
        name = "Center Left-Scale",
        description = "Starting from the center, place a cube in the left scale",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class CenterLScaleAuton extends CommandGroup {

    public CenterLScaleAuton() {

    }

}

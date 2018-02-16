package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;

@Autonomous(
        name = "Right Right-Switch",
        description = "Starting from the right, place a cube in the right switch",
        startingPosition = Autonomous.Side.RIGHT,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class RightRSwitchAuton extends CommandGroup {

    public RightRSwitchAuton() {

    }

}

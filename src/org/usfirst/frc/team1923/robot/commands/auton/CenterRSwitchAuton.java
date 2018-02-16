package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;

@Autonomous(
        name = "Center Right-Switch",
        description = "Starting from the center, place a cube in the right switch",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.RRR, Autonomous.FieldConfiguration.RLR },
        defaultPriority = 50
)
public class CenterRSwitchAuton extends CommandGroup {

    public CenterRSwitchAuton() {

    }

}

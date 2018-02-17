package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;

@Autonomous(
        name = "Center Left-Switch",
        description = "Starting from the center, place a cube in the left switch",
        startingPosition = Autonomous.Side.CENTER,
        fieldConfigurations = { Autonomous.FieldConfiguration.LLL, Autonomous.FieldConfiguration.LRL },
        defaultPriority = 50
)
public class CenterLSwitchAuton extends CommandGroup {

    public CenterLSwitchAuton() {

    }

}

package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightRSwitchAuton extends CommandGroup implements AutonCommand {

    public RightRSwitchAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return allianceSwitch == AllianceColorSide.RIGHT;
    }

}

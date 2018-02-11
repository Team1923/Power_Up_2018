package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterLSwitchAuton extends CommandGroup implements AutonCommand {

    public CenterLSwitchAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return true;
    }

}

package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterRSwitchAuton extends CommandGroup implements AutonCommand {

    public CenterRSwitchAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return true;
    }

}

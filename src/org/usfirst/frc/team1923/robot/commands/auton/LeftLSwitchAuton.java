package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftLSwitchAuton extends CommandGroup implements AutonCommand {

    public LeftLSwitchAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return allianceSwitch == AllianceColorSide.LEFT;
    }

}

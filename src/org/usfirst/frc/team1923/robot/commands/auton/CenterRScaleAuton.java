package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterRScaleAuton extends CommandGroup implements AutonCommand {

    public CenterRScaleAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return true;
    }

}

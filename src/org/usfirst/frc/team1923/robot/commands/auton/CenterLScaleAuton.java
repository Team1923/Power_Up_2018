package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterLScaleAuton extends CommandGroup implements AutonCommand {

    public CenterLScaleAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return true;
    }

}

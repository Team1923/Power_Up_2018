package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightRScaleAuton extends CommandGroup implements AutonCommand {

    public RightRScaleAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return scale == AllianceColorSide.RIGHT;
    }

}

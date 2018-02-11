package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftLScaleAuton extends CommandGroup implements AutonCommand {

    public LeftLScaleAuton() {

    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return scale == AllianceColorSide.LEFT;
    }

}

package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

public class CrossLineShortAuton extends CommandGroup implements AutonCommand {

    public CrossLineShortAuton() {
        this.addSequential(new DriveDistanceCommand(150));
    }

    @Override
    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch) {
        return true;
    }

}

package org.usfirst.frc.team1923.robot.commands.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

public class CrossLineLongAuton extends CommandGroup implements AutonCommand {

    public CrossLineLongAuton() {
        this.addSequential(new DriveDistanceCommand(280));
    }

    @Override
    public boolean isPossible(AutonCommand.AllianceColorSide allianceSwitch, AutonCommand.AllianceColorSide scale, AutonCommand.AllianceColorSide opposingSwitch) {
        return true;
    }

}

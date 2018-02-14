package org.usfirst.frc.team1923.robot.commands.auton;

public interface AutonCommand {

    public boolean isPossible(AllianceColorSide allianceSwitch, AllianceColorSide scale, AllianceColorSide opposingSwitch);

    public enum AllianceColorSide {

        LEFT,
        RIGHT

    }

}

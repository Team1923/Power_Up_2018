package org.usfirst.frc.team1923.commands.auton;

import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CrossTheLineAuton extends CommandGroup {

    /**
     * This class does nothing. It finishes immediately
     */
    public CrossTheLineAuton() {
    	addSequential(new DriveDistanceCommand(10,10));
    }
}
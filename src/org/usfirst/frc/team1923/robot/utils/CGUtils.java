package org.usfirst.frc.team1923.robot.utils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CGUtils {

    public static CommandGroup sequential(Command... commands) {
        CommandGroup commandGroup = new CommandGroup();

        for (Command command : commands) {
            commandGroup.addSequential(command);
        }

        return commandGroup;
    }

    public static CommandGroup parallel(Command... commands) {
        CommandGroup commandGroup = new CommandGroup();

        for (Command command : commands) {
            commandGroup.addParallel(command);
        }

        return commandGroup;
    }

}

package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;


import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
//import java.util.Arrays;
import java.util.Collections;

public class SendablePriorityList extends SendableBase implements Sendable {

    private Map<String, Command> items = new LinkedHashMap();
    private NetworkTableEntry values;

    public void add(Command... commands) {
        for (Command command : commands) {
            this.items.put(command.getClass().getAnnotation(Autonomous.class).name(), command);
        }
    }
    
    public List<Command> getOrder() {
        if (this.values != null) {
            List<Command> commands = new ArrayList<>();
            for (String commandName : values.getStringArray(new String[0])) {
                commands.add(this.items.get(commandName));
            }
            return commands;
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Priority List");
        builder.addStringArrayProperty("values", () -> {
            return this.items.keySet().toArray(new String[0]);
        }, null);
        this.values = builder.getEntry("values");
    }

}

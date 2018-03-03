package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.util.*;

public class SendablePriorityList extends SendableBase implements Sendable {

    private Map<String, Command> items;

    public SendablePriorityList() {
        this.items = new LinkedHashMap<>();
    }

    public void add(Command... commands) {
        for (Command command : commands) {
            this.items.put(command.getClass().getAnnotation(Autonomous.class).name(), command);
        }
    }
    
    public Collection<Command> getOrder() {
        return this.items.values();
    }
    
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Priority List");
        builder.addStringArrayProperty(
                "values",
                () -> this.items.keySet().toArray(new String[0]),
                (String[] values) -> {
                    Map<String, Command> items = this.items;

                    this.items = new LinkedHashMap<>();

                    for (String value : values) {
                        this.items.put(value, items.get(value));
                    }
                }
        );
    }

}

package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.util.LinkedHashMap;
import java.util.Objects;

public class SendableChooser<T> extends SendableBase implements Sendable {

    private final LinkedHashMap<String, T> values;

    private String defaultChoice;
    private String selectedChoice;

    public SendableChooser() {
        this.values = new LinkedHashMap<>();
        this.defaultChoice = "";
        this.selectedChoice = "";
    }

    public void add(String name, T object) {
        if (this.values.isEmpty()) {
            this.addDefault(name, object);
        } else {
            this.addObject(name, object);
        }
    }

    public void addObject(String name, T object) {
        Objects.requireNonNull(name, "Provided name was null");

        this.values.put(name, object);
    }

    public void addDefault(String name, T object) {
        Objects.requireNonNull(name, "Provided name was null");

        this.defaultChoice = name;

        this.addObject(name, object);
    }

    public T getSelected() {
        String selected = this.selectedChoice == null || this.selectedChoice.equals("") ? this.defaultChoice : this.selectedChoice;

        return this.values.get(selected);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("String Chooser");

        builder.addStringProperty(
                "default",
                () -> SendableChooser.this.defaultChoice,
                null
        );

        builder.addStringProperty(
                "selected",
                () -> this.selectedChoice == null || this.selectedChoice.equals("") ? this.defaultChoice : this.selectedChoice,
                (String value) -> this.selectedChoice = value
        );

        builder.addStringArrayProperty(
                "options",
                () -> SendableChooser.this.values.keySet().toArray(new String[0]),
                null
        );
    }

}

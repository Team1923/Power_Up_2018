package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Preset {

    @AutonomousPreset(
            name = "Default"
    )
    DEFAULT();

    private List<Class<?>> classes;

    private Preset(Class<?>... classes) {
        this.classes = new ArrayList<>();

        Collections.addAll(this.classes, classes);

        if (this.classes.isEmpty()) {
            for (Command command : Robot.autonManager.getAllAutons()) {
                this.classes.add(command.getClass());
            }
        }
    }

    public List<Class<?>> getClasses() {
        return this.classes;
    }

}

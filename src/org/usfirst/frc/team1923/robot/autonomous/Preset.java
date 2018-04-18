package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.command.Command;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.commands.auton.CrossLineAuton;
import org.usfirst.frc.team1923.robot.commands.auton.left.LeftLScaleAuton;
import org.usfirst.frc.team1923.robot.commands.auton.left.LeftParkCenterAuton;

public enum Preset {

    @AutonomousPreset(
            name = "Default"
    )
    DEFAULT(),

    @AutonomousPreset(
            name = "Playoff Scale"
    )
    PLAYOFF_SCALE(
            LeftLScaleAuton.class,
            LeftParkCenterAuton.class,
            CrossLineAuton.class
    );

    private List<Class<?>> classes;

    private Preset(Class<?>... classes) {
        this.classes = new ArrayList<>();

        Collections.addAll(this.classes, classes);

        if (this.classes.isEmpty()) {
            for (Command command : Robot.autonManager.getAllAutons()) {
                this.classes.add(command.getClass());
            }

            this.classes.sort((Class<?> c1, Class<?> c2) -> Integer.compare(c2.getAnnotation(Autonomous.class).defaultPriority(), c1.getAnnotation(Autonomous.class).defaultPriority()));
        }
    }

    public List<Class<?>> getClasses() {
        return this.classes;
    }

    @Inherited
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AutonomousPreset {

        String name();

        Autonomous.Side[] startingPosition() default { Autonomous.Side.NONE, Autonomous.Side.LEFT, Autonomous.Side.CENTER, Autonomous.Side.RIGHT };

    }

}

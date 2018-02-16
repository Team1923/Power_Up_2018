package org.usfirst.frc.team1923.robot.autonomous;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutonomousPreset {

    String name();

    Autonomous.Side[] startingPosition() default { Autonomous.Side.NONE, Autonomous.Side.LEFT, Autonomous.Side.CENTER, Autonomous.Side.RIGHT };

}

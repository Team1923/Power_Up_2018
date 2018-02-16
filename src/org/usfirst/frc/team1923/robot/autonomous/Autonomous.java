package org.usfirst.frc.team1923.robot.autonomous;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autonomous {

    String name();

    String description() default "Autonomous Command";

    Side[] startingPosition() default { Side.LEFT, Side.CENTER, Side.RIGHT };

    FieldConfiguration[] fieldConfigurations() default { FieldConfiguration.LLL, FieldConfiguration.RRR, FieldConfiguration.LRL, FieldConfiguration.RLR };

    int defaultPriority() default 100;

    public enum Side {
        LEFT,
        CENTER,
        RIGHT,
        NONE // Default side, will result in DoNothingAuton
    }

    public enum FieldConfiguration {
        LLL,
        RRR,
        LRL,
        RLR
    }

}

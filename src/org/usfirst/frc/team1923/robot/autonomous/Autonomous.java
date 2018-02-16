package org.usfirst.frc.team1923.robot.autonomous;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autonomous {

    Side[] startingPosition() default { Side.LEFT, Side.CENTER, Side.RIGHT };

    FieldConfiguration[] fieldConfigurations() default { FieldConfiguration.LLL, FieldConfiguration.RRR, FieldConfiguration.LRL, FieldConfiguration.RLR };

    public enum Side {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum FieldConfiguration {
        LLL,
        RRR,
        LRL,
        RLR
    }

}

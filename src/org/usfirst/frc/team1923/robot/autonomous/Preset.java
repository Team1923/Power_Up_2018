package org.usfirst.frc.team1923.robot.autonomous;

import org.usfirst.frc.team1923.robot.commands.auton.*;

public enum Preset {

    @AutonomousPreset(
            name = "Default"
    )
    NONE_DEFAULT(
            DoNothingAuton.class
    ),

    @AutonomousPreset(
            name = "Default",
            startingPosition = Autonomous.Side.LEFT
    )
    LEFT_DEFAULT(
            LeftLScaleAuton.class,
            LeftLSwitchAuton.class,
            CrossLineLongAuton.class,
            DoNothingAuton.class
    ),

    @AutonomousPreset(
            name = "Default",
            startingPosition = Autonomous.Side.CENTER
    )
    CENTER_DEFAULT(
            CenterLSwitchAuton.class,
            CenterRSwitchAuton.class,
            CenterLScaleAuton.class,
            CenterRScaleAuton.class,
            CrossLineShortAuton.class,
            DoNothingAuton.class
    ),

    @AutonomousPreset(
            name = "Default",
            startingPosition = Autonomous.Side.RIGHT
    )
    RIGHT_DEFAULT(
            RightRScaleAuton.class,
            RightRSwitchAuton.class,
            CrossLineLongAuton.class,
            DoNothingAuton.class
    );

    private Class[] classes;

    private Preset(Class... classes) {
        this.classes = classes;
    }

}

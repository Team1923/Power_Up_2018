package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.usfirst.frc.team1923.robot.commands.auton.DoNothingAuton;

public class AutonManager {

    private List<Command> autons;

    private SendablePriorityList autonList;
    private SendableChooser<Preset> autonPreset;
    private SendableChooser<Autonomous.Side> robotPosition;

    private Autonomous.Side lastRobotPosition;
    private Preset lastAutonPreset;

    private Notifier notifier;

    public AutonManager() {
        this.autons = new ArrayList<>();

        this.autonList = new SendablePriorityList();
        this.autonPreset = new SendableChooser<>();
        this.robotPosition = new SendableChooser<>();

        this.robotPosition.addDefault("None", Autonomous.Side.NONE);
        this.robotPosition.addObject("Left", Autonomous.Side.LEFT);
        this.robotPosition.addObject("Center", Autonomous.Side.CENTER);
        this.robotPosition.addObject("Right", Autonomous.Side.RIGHT);

        SmartDashboard.putData("Robot Position", this.robotPosition);
    }

    public void start() {
        if (this.notifier == null) {
            this.notifier = new Notifier(this::periodic);
            this.notifier.startPeriodic(0.1);
        }
    }

    private void periodic() {
        Autonomous.Side robotPosition = this.robotPosition.getSelected();
        Preset autonPreset = this.autonPreset.getSelected();

        if (robotPosition != this.lastRobotPosition) {
            this.updatePresets();
            this.updateAutons();

            this.lastRobotPosition = robotPosition;
        }

        if (autonPreset != this.lastAutonPreset) {
            this.updateAutons();

            this.lastAutonPreset = autonPreset;
        }
    }

    public void updatePresets() {
        this.autonPreset = new SendableChooser<>();
        Autonomous.Side robotPosition = this.robotPosition.getSelected();

        for (Preset preset : Preset.values()) {
            try {
                Field field = preset.getClass().getField(preset.name());

                if (!field.isAnnotationPresent(Preset.AutonomousPreset.class)) {
                    continue;
                }

                for (Autonomous.Side startingPosition : field.getAnnotation(Preset.AutonomousPreset.class).startingPosition()) {
                    if (startingPosition == robotPosition) {
                        this.autonPreset.add(field.getAnnotation(Preset.AutonomousPreset.class).name(), preset);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SmartDashboard.putData("Auton Preset", this.autonPreset);
    }

    public void updateAutons() {
        this.autonList = new SendablePriorityList();
        Preset preset = this.autonPreset.getSelected();

        for (Class<?> clazz : preset.getClasses()) {
            if (!clazz.isAnnotationPresent(Autonomous.class)) {
                continue;
            }

            for (Autonomous.Side startingPosition : clazz.getAnnotation(Autonomous.class).startingPosition()) {
                if (startingPosition == this.robotPosition.getSelected()) {
                    for (Command command : this.autons) {
                        if (command.getClass().equals(clazz)) {
                            this.autonList.add(command);
                        }
                    }
                }
            }
        }

        SmartDashboard.putData("Auton Priority List", this.autonList);
    }

    public Command getSelectedAuton() {
        Autonomous.FieldConfiguration currentFieldConfiguration;
        Autonomous.Side startingPosition = this.robotPosition.getSelected();
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        Collection<Command> autonList = this.autonList.getOrder();

        try {
            currentFieldConfiguration = Autonomous.FieldConfiguration.valueOf(gameData);

            System.out.println("[AutonManager @ " + System.currentTimeMillis() + "] Using GameData: " + currentFieldConfiguration.name() + " (Parsed from " + gameData + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("[AutonManager @ " + System.currentTimeMillis() + "] Could not use GameData: \"" + gameData + "\"");
            return null;
        }

        StringBuilder autonListString = new StringBuilder();
        autonListString.append("[");

        for (Command command : autonList) {
            autonListString.append(command.getClass().getSimpleName()).append(", ");
        }

        System.out.println("[AutonManager @ " + System.currentTimeMillis() + "] Using selected StartingPosition: \"" + startingPosition.name() + "\"");
        System.out.println("[AutonManager @ " + System.currentTimeMillis() + "] Using selected AutonList: " + autonListString.toString().replaceAll(", $", "") + "]");

        for (Command command : autonList) {
            for (Autonomous.Side side : command.getClass().getAnnotation(Autonomous.class).startingPosition()) {
                if (side == startingPosition) {

                    for (Autonomous.FieldConfiguration fieldConfiguration : command.getClass().getAnnotation(Autonomous.class).fieldConfigurations()) {

                        if (fieldConfiguration == currentFieldConfiguration) {
                            System.out.println("[AutonManager @ " + System.currentTimeMillis() + "] Selecting auton: \"" + command.getClass().getName() + "\"");

                            return command;
                        }

                    }

                }
            }
        }

        System.out.println("[AutonManager @ " + System.currentTimeMillis() + "] Selecting default auton: \"DoNothingAuton\"");

        return new DoNothingAuton();
    }

    public AutonManager add(Command command) {
        if (this.autons.contains(command)) {
            return this;
        }

        if (!command.getClass().isAnnotationPresent(Autonomous.class)) {
            return this;
        }

        this.autons.add(command);

        return this;
    }

    public List<Command> getAllAutons() {
        return this.autons;
    }

}

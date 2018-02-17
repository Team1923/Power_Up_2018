package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1923.robot.commands.auton.DoNothingAuton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AutonManager {

    private List<Command> autons;

    private SendablePriorityList autonList;
    private SendableChooser<Preset> autonPreset;
    private SendableChooser<Autonomous.Side> robotPosition;

    private Autonomous.Side lastRobotPosition;
    private Preset lastAutonPreset;

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

    public void periodic() {
        Autonomous.Side robotPosition = this.robotPosition.getSelected();
        Preset autonPreset = this.autonPreset.getSelected();

        if (robotPosition != this.lastRobotPosition) {
            this.updatePresets();

            this.lastRobotPosition = robotPosition;
        }

        if (autonPreset != this.lastAutonPreset) {
            this.updateAutons();

            this.lastAutonPreset = autonPreset;
        }
    }

    public void updatePresets() {
        this.autonPreset = new SendableChooser<>(); // TODO: Best way to clear SendableChooser ???
        Autonomous.Side robotPosition = this.robotPosition.getSelected();

        for (Preset preset : Preset.values()) {
            try {
                Field field = preset.getClass().getField(preset.name());

                if (!field.isAnnotationPresent(AutonomousPreset.class)) {
                    continue;
                }

                for (Autonomous.Side startingPosition : field.getAnnotation(AutonomousPreset.class).startingPosition()) {
                    if (startingPosition == robotPosition) {
                        if (preset == Preset.DEFAULT) {
                            this.autonPreset.addDefault(field.getAnnotation(AutonomousPreset.class).name(), preset);
                        } else {
                            this.autonPreset.addObject(field.getAnnotation(AutonomousPreset.class).name(), preset);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SmartDashboard.putData("Auton Preset", this.autonPreset);
    }

    public void updateAutons() {
        this.autonList = new SendablePriorityList(); // TODO: Best way to clear SendablePriorityList ???
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
        Autonomous.FieldConfiguration currentFieldConfiguration = Autonomous.FieldConfiguration.valueOf(DriverStation.getInstance().getGameSpecificMessage());

        for (Command command : this.autonList.getOrder()) {
            for (Autonomous.Side side : command.getClass().getAnnotation(Autonomous.class).startingPosition()) {
                if (side == this.robotPosition.getSelected()) {

                    for (Autonomous.FieldConfiguration fieldConfiguration : command.getClass().getAnnotation(Autonomous.class).fieldConfigurations()) {

                        if (fieldConfiguration == currentFieldConfiguration) {
                            return command;
                        }

                    }

                }
            }
        }

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
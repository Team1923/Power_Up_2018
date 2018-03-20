package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeToggleCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class OI {

    public PS4Controller driver;
    public XboxController operator;

    public OI() {
        this.driver = new PS4Controller(RobotMap.Controller.DRIVER_PORT);
        this.driver.leftTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);
        this.driver.rightTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);

        this.operator = new XboxController(RobotMap.Controller.OPERATOR_PORT);
        this.operator.leftTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);
        this.operator.rightTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);

        this.operator.y.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP));
        this.operator.b.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.operator.a.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));
        this.operator.x.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SCALE));

        this.operator.leftButton.whenPressed(IntakeToggleCommand.getInstance());
        this.operator.rightButton.whenPressed(IntakeToggleCommand.getInstance());
        this.operator.dPad.down.whenPressed(new IntakeLowerCommand());
        this.operator.dPad.up.whenPressed(new IntakeRaiseCommand());

        this.driver.circle.whenPressed(new DriveTrajectoryCommand(TrajectoryStore.Waypoints.CENTER_RSCALE));
    }

}

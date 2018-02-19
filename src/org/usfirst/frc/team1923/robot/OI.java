package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeCloseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOpenCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;

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

        this.operator.y.whenPressed(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP));
        this.operator.b.whenPressed(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SWITCH));
        this.operator.a.whenPressed(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));
        this.operator.x.whenPressed(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.SCALE));

        this.operator.leftButton.whenPressed(new IntakeCloseCommand());
        this.operator.rightButton.whenPressed(new IntakeOpenCommand());
        this.operator.dPad.down.whenPressed(new IntakeLowerCommand());
        this.operator.dPad.up.whenPressed(new IntakeRaiseCommand());
    }

}

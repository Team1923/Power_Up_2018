package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.commands.intake.IntakeCloseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOpenCommand;
import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;

public class OI {

    public PS4Controller driver;
    public XboxController operator;

    public OI() {
        this.driver = new PS4Controller(RobotMap.DRIVER_CONTROLLER_PORT);
        this.driver.leftTrigger.setTriggerSensitivity(0.5);
        this.driver.rightTrigger.setTriggerSensitivity(0.5);

        this.operator = new XboxController(RobotMap.OPERATOR_CONTROLLER_PORT);
        this.operator.leftTrigger.setTriggerSensitivity(0.5);
        this.operator.rightTrigger.setTriggerSensitivity(0.5);

        this.operator.leftButton.whenPressed(new IntakeOpenCommand());
        this.operator.rightButton.whenPressed(new IntakeCloseCommand());
    }

}

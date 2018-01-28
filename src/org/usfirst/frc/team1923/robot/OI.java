package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTimeCommand;

public class OI {

    public  PS4Controller driver;
    public XboxController operator;

    public OI() {
        this.driver = new PS4Controller(RobotMap.DRIVER_CONTROLLER_PORT);
        this.operator = new XboxController(RobotMap.OPERATOR_CONTROLLER_PORT);

        this.driver.leftTrigger.setTriggerSensitivity(0.5);
        this.driver.rightTrigger.setTriggerSensitivity(0.5);

        this.operator.leftTrigger.setTriggerSensitivity(0.5);
        this.operator.rightTrigger.setTriggerSensitivity(0.5);
    }

}

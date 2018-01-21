package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;

public class OI {

    public  PS4Controller driver = new  PS4Controller(RobotMap.DRIVER_CONTROLLER_PORT);
    public XboxController op = new XboxController(RobotMap.OPERATOR_CONTROLLER_PORT);

    public OI() {
        this.driver.lt.setTriggerSensitivity(0.5);
        this.driver.rt.setTriggerSensitivity(0.5);
    }

}

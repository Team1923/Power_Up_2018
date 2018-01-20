package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;

public class OI {
    public PS4Controller driver;
    public XboxController op;

    public OI() {
        this.driver = new PS4Controller(RobotMap.DRIVER_CONTROLLER_PORT);
        this.driver.lt.setTriggerSensitivity(0.5);
        this.driver.rt.setTriggerSensitivity(0.5);

//      this.driver.lb.whenActive(new ShiftCommand(false));
//      this.driver.rb.whenActive(new ShiftCommand(true));

//      this.driver.lt.whenActive(new ShiftOmniCommand(true));
//      this.driver.rt.whenActive(new ShiftOmniCommand(false));

//      this.driver.cross.whenActive(new ResetEncoderCommand());

        this.op = new XboxController(RobotMap.OPERATOR_CONTROLLER_PORT);

//      this.op.x.whenActive(new SlideCommand());
//      this.op.y.whenActive(new GearCommand());
//      this.op.b.whenActive(new GearSetHomeCommand());

//      Command pegAlign = new VisionAlignCommand(false);
//      driver.square.whileHeld(pegAlign);
//      Command feederAlign = new VisionAlignCommand(true);
//      driver.triangle.whileHeld(feederAlign);
//      Command refresh = new VisionProcessing();
//      driver.circle.whileHeld(refresh);
    }

}

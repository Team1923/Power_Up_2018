package org.usfirst.frc.team1923.robot.utils.battery.device;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.usfirst.frc.team1923.robot.utils.battery.OutputDevice;

public class LTalonSRX extends OutputDevice {

    private TalonSRX talon;
    private ControlMode controlMode;

    public LTalonSRX(int deviceId, int priority) {
        super(priority);

        this.talon = new TalonSRX(deviceId);
    }

    @Override
    public double getCurrent(double output) {
        return 0;
    }

    public void setOutput(ControlMode controlMode, double output) {
        super.setOutput(output);

        this.controlMode = controlMode;
        this.talon.set(this.controlMode, this.getOutput());
    }

}

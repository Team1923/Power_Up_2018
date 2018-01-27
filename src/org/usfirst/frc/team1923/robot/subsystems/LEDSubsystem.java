package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SerialPort;

import org.usfirst.frc.team1923.robot.commands.led.LEDCommand;

public class LEDSubsystem extends Subsystem {

    public enum Mode {
        OFF("B"),
        ON("A");

        private String data;

        private Mode(String data) {
            this.data = data;
        }

        public String getData() {
            return this.data;
        }
    }

    private SerialPort arduino;
    private Mode mode;

    public LEDSubsystem() {
        this.mode = Mode.OFF;

        try {
            this.arduino = new SerialPort(9600, SerialPort.Port.kUSB);
        } catch (Exception e) {
            this.arduino = null;
        }
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new LEDCommand());
    }

    public void setMode(Mode mode) {
        this.mode = mode;

        if (this.arduino != null) {
            this.arduino.writeString(this.mode.getData());
            this.arduino.flush();
        }
    }

    public Mode getCurrentMode() {
        return this.mode;
    }

}

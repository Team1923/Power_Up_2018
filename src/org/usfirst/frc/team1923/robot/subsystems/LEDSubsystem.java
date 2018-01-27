package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SerialPort;

import org.usfirst.frc.team1923.robot.commands.LEDCommand;

public class LEDSubsystem extends Subsystem {

    public enum Mode {
        On, Off
    }

    private SerialPort arduino;
    public Mode currentMode = Mode.Off;

    @Override
    protected void initDefaultCommand() {
        arduino = new SerialPort(9600, SerialPort.Port.kUSB);
        setDefaultCommand(new LEDCommand());
    }

    public void setMode(Mode mode) {
        currentMode = mode;
        arduino.writeString(mode == Mode.On ? "A" : "B");
        arduino.flush();
    }
}

package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SerialPort;

import org.usfirst.frc.team1923.robot.commands.led.LEDCommand;
import org.usfirst.frc.team1923.robot.ledprofiles.LEDProfile;

public class LEDSubsystem extends Subsystem {

    private SerialPort arduino;
    private LEDProfile profile;

    public LEDSubsystem() {
        try {
            this.arduino = new SerialPort(19200, SerialPort.Port.kUSB);
        } catch (Exception e) {
            this.arduino = null;
        }
    }

    public void setProfile(LEDProfile profile) {
        this.profile = profile;
    }

    public LEDProfile getProfile() {
        return this.profile;
    }

    public SerialPort getArduino() {
        return this.arduino;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new LEDCommand());
    }

}

package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1923.robot.commands.led.LEDOffCommand;

import java.util.Arrays;

// TODO: Cache, Blink, Arduino
public class LEDSubsystem extends Subsystem {

    private static final int BAUD = 19200;
    private static final int NUM_PIXELS = 60;

    public static class LEDMode {
        private int[] colors;

        public static final LEDMode OFF = new LEDMode(0x000000);
        public static final LEDMode ON = new LEDMode(0xFFFFFF);

        public LEDMode(int[] colors) {
            this.colors = colors;
        }

        public LEDMode(int color) {
            this.colors = new int[NUM_PIXELS];
            Arrays.fill(this.colors, color);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof LEDMode)) {
                return false;
            }
            return Arrays.equals(this.colors, ((LEDMode) obj).colors);
        }

        public byte[] toData() {
            byte[] data = new byte[this.colors.length * 3];
            for (int i = 0; i < this.colors.length; ++i) {
                data[i * 3    ] = (byte) (this.colors[i] >> 16);
                data[i * 3 + 1] = (byte) (this.colors[i] >> 8);
                data[i * 3 + 2] = (byte) this.colors[i];
            }
            return data;
        }
    }

    private LEDMode setMode;
    private LEDMode currentMode;
    private boolean modified;
    private int tick;

    private SerialPort arduino;

    public LEDSubsystem() {
        try {
            this.arduino = new SerialPort(BAUD, SerialPort.Port.kUSB);
        } catch (Exception e) {}

        this.tick = 0;
        this.modified = true;
        this.currentMode = LEDMode.OFF;
    }

    @Override
    public void periodic() {
        if (this.arduino != null) {
            if (this.tick == 0) {
                this.tick = 5;
                if (this.modified && !this.currentMode.equals(this.setMode)) {
                    this.setMode = this.currentMode;
                    byte[] data = this.setMode.toData();
                    this.arduino.write(data, data.length);
                }
                this.modified = false;
            }
            --this.tick;
        }
    }

    public void setMode(LEDMode newMode) {
        this.modified = true;
        this.currentMode = newMode;
    }

    public LEDMode getMode() {
        return this.currentMode;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new LEDOffCommand());
    }

}

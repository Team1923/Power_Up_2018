package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SerialPort;

import org.usfirst.frc.team1923.robot.commands.led.LEDCommand;

import java.util.Arrays;

public class LEDSubsystem extends Subsystem {

    private final int BAUD = 19200;

    public static class LEDMode {
        // list of colors e.g.
        // 0xFF0000 is red
        // 0x00FF00 is green
        // 0x0000FF is blue
        private int[] colors;

        private final int NUM_LEDS = 60;

        public final static LEDMode OFF = new LEDMode(0x000000);
        public final static LEDMode ON  = new LEDMode(0xFFFFFF);

        public LEDMode(int[] colors) {
            this.colors = colors;
        }

        public LEDMode(int color) {
            this.colors = new int[NUM_LEDS];
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
            LEDMode mode = (LEDMode) obj;
            return Arrays.equals(this.colors, mode.colors);
        }

        public byte[] toData() {
            byte[] data = new byte[colors.length * 3];
            for (int i = 0; i < colors.length; ++i) {
                data[i * 3    ] = (byte) (colors[i] >> 16);
                data[i * 3 + 1] = (byte) (colors[i] >> 8);
                data[i * 3 + 2] = (byte) colors[i];
            }
            return data;
        }
    }

    public  LEDMode currentMode;
    private LEDMode     setMode;

    private int tick;

    private SerialPort arduino;

    public LEDSubsystem() {
        try {
            this.arduino = new SerialPort(BAUD, SerialPort.Port.kUSB);
        } catch (Exception e) {}

        this.tick = 0;
        this.currentMode = LEDMode.OFF;
    }

    @Override
    protected void initDefaultCommand() {
        this.setDefaultCommand(new LEDCommand());
    }

    @Override
    public void periodic() {
        if (this.arduino != null) {
            if (this.tick == 0) {
                this.tick = 5;
                if (!this.currentMode.equals(this.setMode)) {
                    this.setMode = this.currentMode;
                    byte[] data = this.setMode.toData();
                    this.arduino.write(data, data.length);
                    this.arduino.flush(); // dont know if this is needed
                }
            }
            --this.tick;
        }
    }
}

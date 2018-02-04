
import org.usfirst.frc.team1923.robot.commands.led.LEDCommand;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LEDSubsystem extends Subsystem {

    private SerialPort arduino;
    private int tick;
    private final int BAUD = 19200;
    private boolean modified = false;
    private byte[] data = new byte[10];;
    // list of colors e.g.
    // 0xFF0000 is red
    // 0x00FF00 is green
    // 0x0000FF is blue
    private int[] colors;

    private int pattern = 1;

    public void setColor(int[] new_colors) {
        colors = new_colors.clone();
        this.modified = true;
    }

    public int[] getColor() {
        return this.colors;
    }

    public void setPattern(int new_pattern) {
        this.pattern = new_pattern;
        this.modified = true;
    }

    public int getPattern() {
        return this.pattern;
    }

    public LEDSubsystem() {
        try {
            this.arduino = new SerialPort(BAUD, SerialPort.Port.kUSB);
        } catch (Exception e) {
        }
        this.modified = true;
    }

    @Override
    public void periodic() {
        if (this.arduino != null) {
            if (this.tick == 0) {
                this.tick = 5;
                if (this.modified) {
                    data[0] = (byte) pattern;
                    for (int inc = 1; inc < 4; inc++) {
                        data[inc] = (byte) colors[inc - 1];
                    }
                    this.arduino.write(data, 4);
                    this.arduino.flush(); // dont know if this is needed
                }
                this.modified = false;
            }
            --this.tick;
        }
    }

    @Override
    public void initDefaultCommand() {
        this.setDefaultCommand(new LEDCommand());

    }

}

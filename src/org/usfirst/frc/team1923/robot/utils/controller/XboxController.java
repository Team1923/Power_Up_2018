package org.usfirst.frc.team1923.robot.utils.controller;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxController extends Controller {

    private static final double TRIGGER_DEADZONE = 0.2;

    private static final int A_BUTTON_ID = 1;
    private static final int B_BUTTON_ID = 2;
    private static final int X_BUTTON_ID = 3;
    private static final int Y_BUTTON_ID = 4;
    private static final int LB_BUTTON_ID = 5;
    private static final int RB_BUTTON_ID = 6;
    private static final int BACK_BUTTON_ID = 7;
    private static final int START_BUTTON_ID = 8;
    private static final int LEFT_CLICK_ID = 9;
    private static final int RIGHT_CLICK_ID = 10;

    // TODO: Find correct axis id's
    private static final int LEFT_STICK_X_AXIS_ID = 0;
    private static final int LEFT_STICK_Y_AXIS_ID = 1;
    private static final int RIGHT_STICK_X_AXIS_ID = 4;
    private static final int RIGHT_STICK_Y_AXIS_ID = 5;

    private static final int LEFT_TRIGGER_AXIS_ID = 2;
    private static final int RIGHT_TRIGGER_AXIS_ID = 3;

    public final Trigger leftTrigger;
    public final Trigger rightTrigger;
    public final DirectionalPad dPad;
    public final Button a;
    public final Button b;
    public final Button x;
    public final Button y;
    public final Button leftButton;
    public final Button rightButton;
    public final Button back;
    public final Button start;
    public final Button rightClick;
    public final Button leftClick;

    public XboxController(final int port) {
        super(port);

        this.dPad = new DirectionalPad(this.controller);
        this.leftTrigger = new Trigger(this.controller, LEFT_TRIGGER_AXIS_ID);
        this.rightTrigger = new Trigger(this.controller, RIGHT_TRIGGER_AXIS_ID);
        this.a = new JoystickButton(this.controller, A_BUTTON_ID);
        this.b = new JoystickButton(this.controller, B_BUTTON_ID);
        this.x = new JoystickButton(this.controller, X_BUTTON_ID);
        this.y = new JoystickButton(this.controller, Y_BUTTON_ID);
        this.leftButton = new JoystickButton(this.controller, LB_BUTTON_ID);
        this.rightButton = new JoystickButton(this.controller, RB_BUTTON_ID);
        this.back = new JoystickButton(this.controller, BACK_BUTTON_ID);
        this.start = new JoystickButton(this.controller, START_BUTTON_ID);
        this.rightClick = new JoystickButton(this.controller, RIGHT_CLICK_ID);
        this.leftClick = new JoystickButton(this.controller, LEFT_CLICK_ID);
    }

    /**
     * Adjusted y values based on deadzone
     *
     * @return the adjusted y value
     */
    public double getLeftY() {
        double val = -this.getRawAxis(LEFT_STICK_Y_AXIS_ID);
        return Math.abs(val) > TRIGGER_DEADZONE ? val : 0;
    }

    /**
     * Adjusted y values based on deadzone
     *
     * @return the adjusted y value
     */
    public double getRightY() {
        double val = -this.getRawAxis(RIGHT_STICK_Y_AXIS_ID);
        return Math.abs(val) > TRIGGER_DEADZONE ? val : 0;
    }

    /**
     * Adjusted x values based on deadzone
     *
     * @return the adjusted x value
     */
    public double getLeftX() {
        double val = this.getRawAxis(LEFT_STICK_X_AXIS_ID);
        return Math.abs(val) > TRIGGER_DEADZONE ? val : 0;
    }

    /**
     * Adjusted x values based on deadzone
     *
     * @return the adjusted x value
     */
    public double getRightX() {
        double val = this.getRawAxis(RIGHT_STICK_X_AXIS_ID);
        return Math.abs(val) > TRIGGER_DEADZONE ? val : 0;
    }

    public double getLeftTrigger() {
        return (this.leftTrigger.getX() + 1) / 2;
    }

    public double getRightTrigger() {
        return (this.rightTrigger.getX() + 1) / 2;
    }

}
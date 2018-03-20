package org.usfirst.frc.team1923.robot.utils.controller;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class PS4Controller extends Controller {

    private static final double TRIGGER_DEADZONE = 0.05;

    private static final int SQUARE_BUTTON_ID = 1;
    private static final int TRIANGLE_BUTTON_ID = 4;
    private static final int CIRCLE_BUTTON_ID = 3;
    private static final int CROSS_BUTTON_ID = 2;
    private static final int LEFT_BUTTON_ID = 5; // Also called L1
    private static final int RIGHT_BUTTON_ID = 6; // Also called R1
    private static final int SHARE_BUTTON_ID = 9;
    private static final int OPTIONS_BUTTON_ID = 10;
    private static final int LEFT_CLICK_ID = 11;
    private static final int RIGHT_CLICK_ID = 12;

    private static final int LEFT_STICK_X_AXIS_ID = 0;
    private static final int LEFT_STICK_Y_AXIS_ID = 1;
    private static final int RIGHT_STICK_X_AXIS_ID = 2;
    private static final int RIGHT_STICK_Y_AXIS_ID = 5;

    private static final int LEFT_TRIGGER_AXIS_ID = 3; // Also called L2
    private static final int RIGHT_TRIGGER_AXIS_ID = 4; // Also called R2

    public final Trigger leftTrigger;
    public final Trigger rightTrigger;
    public final DirectionalPad dPad;
    public final Button square;
    public final Button triangle;
    public final Button circle;
    public final Button cross;
    public final Button leftButton;
    public final Button rightButton;
    public final Button share;
    public final Button options;
    public final Button rightClick;
    public final Button leftClick;

    public PS4Controller(final int port) {
        super(port);

        this.dPad = new DirectionalPad(this.controller);
        this.leftTrigger = new Trigger(this.controller, LEFT_TRIGGER_AXIS_ID);
        this.rightTrigger = new Trigger(this.controller, RIGHT_TRIGGER_AXIS_ID);
        this.square = new JoystickButton(this.controller, SQUARE_BUTTON_ID);
        this.triangle = new JoystickButton(this.controller, TRIANGLE_BUTTON_ID);
        this.circle = new JoystickButton(this.controller, CIRCLE_BUTTON_ID);
        this.cross = new JoystickButton(this.controller, CROSS_BUTTON_ID);
        this.leftButton = new JoystickButton(this.controller, LEFT_BUTTON_ID);
        this.rightButton = new JoystickButton(this.controller, RIGHT_BUTTON_ID);
        this.share = new JoystickButton(this.controller, SHARE_BUTTON_ID);
        this.options = new JoystickButton(this.controller, OPTIONS_BUTTON_ID);
        this.rightClick = new JoystickButton(this.controller, RIGHT_CLICK_ID);
        this.leftClick = new JoystickButton(this.controller, LEFT_CLICK_ID);

        this.leftTrigger.setTriggerDeadZone(TRIGGER_DEADZONE);
        this.rightTrigger.setTriggerDeadZone(TRIGGER_DEADZONE);
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

}
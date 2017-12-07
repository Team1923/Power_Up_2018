package org.usfirst.frc.team1923.robot.utils.controller;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxController extends Controller {

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

    private static final int LEFT_TRIGGER_AXIS_ID = 2;
    private static final int RIGHT_TRIGGER_AXIS_ID = 3;

    public final Trigger lt;
    public final Trigger rt;
    public final DirectionalPad dPad;
    public final Button a;
    public final Button b;
    public final Button x;
    public final Button y;
    public final Button lb;
    public final Button rb;
    public final Button back;
    public final Button start;
    public final Button rightClick;
    public final Button leftClick;

    public XboxController(final int port) {
        super(port);

        this.dPad = new DirectionalPad(this.controller);
        this.lt = new Trigger(this.controller, LEFT_TRIGGER_AXIS_ID);
        this.rt = new Trigger(this.controller, RIGHT_TRIGGER_AXIS_ID);
        this.a = new JoystickButton(this.controller, A_BUTTON_ID);
        this.b = new JoystickButton(this.controller, B_BUTTON_ID);
        this.x = new JoystickButton(this.controller, X_BUTTON_ID);
        this.y = new JoystickButton(this.controller, Y_BUTTON_ID);
        this.lb = new JoystickButton(this.controller, LB_BUTTON_ID);
        this.rb = new JoystickButton(this.controller, RB_BUTTON_ID);
        this.back = new JoystickButton(this.controller, BACK_BUTTON_ID);
        this.start = new JoystickButton(this.controller, START_BUTTON_ID);
        this.rightClick = new JoystickButton(this.controller, RIGHT_CLICK_ID);
        this.leftClick = new JoystickButton(this.controller, LEFT_CLICK_ID);
    }

}
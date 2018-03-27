package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.drive.TurnGyroCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorPositionCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeLowerCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeRaiseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeTimeCommand;
import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class OI {

    public PS4Controller driver;
    public XboxController operator;

    public OI() {
        this.driver = new PS4Controller(RobotMap.Controller.DRIVER_PORT);
        this.driver.leftTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);
        this.driver.rightTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);

        this.operator = new XboxController(RobotMap.Controller.OPERATOR_PORT);
        this.operator.leftTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);
        this.operator.rightTrigger.setTriggerSensitivity(RobotMap.Controller.TRIGGER_SENSITIVITY);

        this.operator.y.whileHeld(new IntakeTimeCommand(0.75, Integer.MAX_VALUE));
        this.operator.b.whileHeld(new IntakeTimeCommand(0.50, Integer.MAX_VALUE));
        this.operator.x.whileHeld(new IntakeTimeCommand(0.25, Integer.MAX_VALUE));
        this.operator.a.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));

        this.operator.dPad.down.whenPressed(new IntakeLowerCommand());
        this.operator.dPad.up.whenPressed(new IntakeRaiseCommand());

        this.operator.dPad.left.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.BOTTOM));
        this.operator.dPad.right.whileHeld(new ElevatorPositionCommand(ElevatorPositionCommand.ElevatorPosition.TOP));

        this.driver.circle.whileHeld(new DriveTrajectoryCommand(TrajectoryStore.Path.LEFT_RSCALE));
    }

}

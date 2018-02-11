package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.commands.drive.DriveDistanceCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTimeCommand;
import org.usfirst.frc.team1923.robot.commands.drive.DriveTrajectoryCommand;
import org.usfirst.frc.team1923.robot.commands.elevator.ElevatorMoveCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeCloseCommand;
import org.usfirst.frc.team1923.robot.commands.intake.IntakeOpenCommand;
import org.usfirst.frc.team1923.robot.utils.controller.PS4Controller;
import org.usfirst.frc.team1923.robot.utils.controller.XboxController;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class OI {

    public PS4Controller driver;
    public XboxController operator;

    public OI() {
        this.driver = new PS4Controller(RobotMap.DRIVER_CONTROLLER_PORT);
        this.driver.leftTrigger.setTriggerSensitivity(0.5);
        this.driver.rightTrigger.setTriggerSensitivity(0.5);

        this.operator = new XboxController(RobotMap.OPERATOR_CONTROLLER_PORT);
        this.operator.leftTrigger.setTriggerSensitivity(0.5);
        this.operator.rightTrigger.setTriggerSensitivity(0.5);
        
        Trajectory traj = Pathfinder.generate(new Waypoint[] {
        		new Waypoint(0, 0, 0),
        		new Waypoint(3, 0, 0)
        }, TrajectoryStore.trajectoryConfig);

        this.driver.circle.whileHeld(new DriveTrajectoryCommand(traj));
    }

}

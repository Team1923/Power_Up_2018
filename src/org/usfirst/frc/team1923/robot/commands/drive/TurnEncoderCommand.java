package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.RobotMap;

public class TurnEncoderCommand extends DriveDistanceCommand {

    public TurnEncoderCommand(double angle) {
        super(
                -angle * Math.PI * RobotMap.Drivetrain.WHEELBASE_WIDTH / 360.0 * 0.85,
                angle * Math.PI * RobotMap.Drivetrain.WHEELBASE_WIDTH / 360.0 * 0.85
        );

        this.setTimeout(1.5);
    }

}

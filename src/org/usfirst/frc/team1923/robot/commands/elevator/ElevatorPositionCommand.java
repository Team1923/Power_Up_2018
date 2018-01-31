package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Move the elevator to a set position
 */
public class ElevatorPositionCommand extends Command {

    public ElevatorPositionCommand() {
        this.requires(Robot.elevatorSubsystem);
    }

    protected void initialize() {
    		
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return Robot.oi.operator.getLeftY() > 0.3;
    }

    protected void end() {
        Robot.elevatorSubsystem.stop();
    }

    protected void interrupted() {
        this.end();
    }

}

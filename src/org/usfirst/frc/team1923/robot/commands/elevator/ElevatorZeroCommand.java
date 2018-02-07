package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

/**
 * Lower the elevator to starting position and zero the encoders.
 */
public class ElevatorZeroCommand extends Command {

    public ElevatorZeroCommand() {
        this.requires(Robot.elevatorSubsystem);
    }

    protected void initialize() {}

    protected void execute() {}
    
    protected boolean isFinished() {
        return Robot.elevatorSubsystem.isZeroed();
    }

    protected void start() {
    		Robot.elevatorSubsystem.set(ControlMode.Velocity, -0.5);
    }

    protected void end() {
        Robot.elevatorSubsystem.getTalons()[0].setPosition(0);
        Robot.elevatorSubsystem.stop();
    }

    protected void interrupted() {
        this.end();
    }

}

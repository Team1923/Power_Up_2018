package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;

public class ElevatorMoveCommand extends Command {

    private double out;

    public ElevatorMoveCommand(double out) {
        this.requires(Robot.elevatorSubsystem);

        this.out = out;
    }

    @Override
    protected void execute() {
        Robot.elevatorSubsystem.set(ControlMode.PercentOutput, this.out);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.elevatorSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        Robot.elevatorSubsystem.stop();
    }

}

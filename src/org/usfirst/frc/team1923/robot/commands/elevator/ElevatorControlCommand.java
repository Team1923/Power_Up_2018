package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;

/**
 * Change elevator position based on operator joystick values.
 */
public class ElevatorControlCommand extends Command {

    public ElevatorControlCommand() {
        this.requires(Robot.elevatorSubsystem);
    }

    @Override
    protected void execute() {
        double outputPower = Robot.oi.operator.getLeftTrigger() > 0 ? -Robot.oi.operator.getLeftTrigger() : Robot.oi.operator.getRightTrigger();

        double stoppingTime = Math.abs(Robot.elevatorSubsystem.getElevatorVelocity()) / RobotMap.Elevator.MAX_ACCELERATION;
        double stoppingDistance = Math.abs(Robot.elevatorSubsystem.getElevatorVelocity()) * stoppingTime + 0.5 * -RobotMap.Elevator.MAX_ACCELERATION * Math.pow(stoppingTime, 2);

        double distanceLeft = outputPower < 0 ? Robot.elevatorSubsystem.getElevatorPosition() : (RobotMap.Elevator.PRIMARY_STAGE_TRAVEL + RobotMap.Elevator.SECONDARY_STAGE_TRAVEL) - Robot.elevatorSubsystem.getElevatorPosition();

        if (distanceLeft < stoppingDistance) {
            double calculatedVelocity = Math.sqrt(2 * RobotMap.Elevator.MAX_ACCELERATION * distanceLeft);
            double calculatedOutputPower = RobotMap.Elevator.VELOCITY_CONSTANT * calculatedVelocity;

            if (Math.abs(outputPower) > calculatedOutputPower) {
                outputPower = (outputPower / Math.abs(outputPower)) * calculatedOutputPower;
            }
        }

        Robot.elevatorSubsystem.set(ControlMode.PercentOutput, outputPower);
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

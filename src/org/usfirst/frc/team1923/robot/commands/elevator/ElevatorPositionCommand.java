package org.usfirst.frc.team1923.robot.commands.elevator;

import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.ElevatorSubsystem;

/**
 * Move the elevator to a set position
 */
public class ElevatorPositionCommand extends Command {

    private double position;
    private int targetEncoderTick;

    public ElevatorPositionCommand(double position) {
        this.requires(Robot.elevatorSubsystem);
        this.position = position;
        SmartDashboard.putNumber("position to run", position);
    }

    protected void initialize() {
        this.position = SmartDashboard.getNumber("position to run", position);
        this.targetEncoderTick = (int) (position * 4000); // to do
        Robot.elevatorSubsystem.set(ControlMode.MotionMagic, this.targetEncoderTick);
    }

    protected void execute() {
        // Only for debugging purposes
    }

    protected boolean isFinished() {
        return (Math.abs(this.position - this.targetEncoderTick) < 300 && Math.abs(this.position - this.targetEncoderTick) < 300);
    }

    protected void end() {
        Robot.elevatorSubsystem.stop();
    }

    protected void interrupted() {
        this.end();
    }

}

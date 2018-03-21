package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;

/**
 * Drive the robot for a distance (in inches) using TalonSRX Motion Magic.
 */
public class DriveDistanceCommand extends Command {

    private double leftTarget;
    private double rightTarget;

    private double velocity;
    private double acceleration;

    public DriveDistanceCommand(double distance) {
        this(distance, distance, 10000);
    }

    public DriveDistanceCommand(double leftTarget, double rightTarget, double timeout) {
        this(leftTarget, rightTarget, RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.MM_MAX_ACCELERATION, timeout);
    }

    public DriveDistanceCommand(double leftTarget, double rightTarget, double velocity, double acceleration, double timeout) {
        this.requires(Robot.drivetrainSubsystem);

        this.leftTarget = Converter.inchesToTicks(leftTarget, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.rightTarget = Converter.inchesToTicks(rightTarget, RobotMap.Drivetrain.WHEEL_DIAMETER);

        this.setTimeout(timeout);

        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetPosition();

        Robot.drivetrainSubsystem.getLeftMasterTalon().configMotionAcceleration(Converter.inchesToTicks(this.acceleration, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getLeftMasterTalon().configMotionCruiseVelocity(Converter.inchesToTicks(this.velocity, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getRightMasterTalon().configMotionAcceleration(Converter.inchesToTicks(this.acceleration, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getRightMasterTalon().configMotionCruiseVelocity(Converter.inchesToTicks(this.velocity, RobotMap.Drivetrain.WHEEL_DIAMETER) / 10, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive(ControlMode.MotionMagic, this.leftTarget, this.rightTarget);
    }

    @Override
    protected boolean isFinished() {
        return this.isTimedOut()
                || Math.abs(this.leftTarget - Robot.drivetrainSubsystem.getLeftEncoderPosition()) < RobotMap.Drivetrain.MM_ALLOWABLE_ERROR
                && Math.abs(this.rightTarget - Robot.drivetrainSubsystem.getRightEncoderPosition()) < RobotMap.Drivetrain.MM_ALLOWABLE_ERROR;
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

}

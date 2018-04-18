package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

/**
 * Drive the robot for a distance (in inches) using TalonSRX Motion Magic.
 */
public class DriveDistanceCommand extends Command {

    private double leftDistance;
    private double rightDistance;

    private double velocity;
    private double acceleration;

    public DriveDistanceCommand(double distance) {
        this(distance, distance);
    }

    public DriveDistanceCommand(double leftDistance, double rightDistance) {
        this(leftDistance, rightDistance, RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.MM_MAX_ACCELERATION, Integer.MAX_VALUE);
    }

    public DriveDistanceCommand(double leftDistance, double rightDistance, double velocity, double acceleration, double timeout) {
        this.requires(Robot.drivetrainSubsystem);
        this.setTimeout(timeout);

        this.leftDistance = Converter.inchesToTicks(leftDistance, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.rightDistance = Converter.inchesToTicks(rightDistance, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public DriveDistanceCommand setVelocity(double velocity) {
        this.velocity = velocity;

        return this;
    }

    public DriveDistanceCommand setAcceleration(double acceleration) {
        this.acceleration = acceleration;

        return this;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.configureDriving();

        int constant = 10;

        if (this.leftDistance == this.rightDistance) {
            constant = 5;
        } else {
            Robot.drivetrainSubsystem.configureIndividualSensor();
        }

        Robot.drivetrainSubsystem.getLeftMaster().configMotionAcceleration(Converter.inchesToTicks(this.acceleration, RobotMap.Drivetrain.WHEEL_DIAMETER) / constant, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getLeftMaster().configMotionCruiseVelocity(Converter.inchesToTicks(this.velocity, RobotMap.Drivetrain.WHEEL_DIAMETER) / constant, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getRightMaster().configMotionAcceleration(Converter.inchesToTicks(this.acceleration, RobotMap.Drivetrain.WHEEL_DIAMETER) / constant, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getRightMaster().configMotionCruiseVelocity(Converter.inchesToTicks(this.velocity, RobotMap.Drivetrain.WHEEL_DIAMETER) / constant, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    @Override
    protected void execute() {
        if (this.leftDistance == this.rightDistance) {
            Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.MotionMagic, this.leftDistance * 2.0, DemandType.AuxPID, 0.0);
            Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionMagic, this.rightDistance * 2.0, DemandType.AuxPID, 0.0);
        } else {
            Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.MotionMagic, this.leftDistance);
            Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionMagic, this.rightDistance);
        }

        System.out.println("Left: " + Robot.drivetrainSubsystem.getLeftMaster().getMotorOutputPercent() + ", " + ", Right: " + Robot.drivetrainSubsystem.getRightMaster().getMotorOutputPercent());
    }

    @Override
    protected boolean isFinished() {
        return this.isTimedOut() || this.isAlmostFinished(RobotMap.Drivetrain.MM_ALLOWABLE_ERROR);
    }

    public boolean isAlmostFinished(int encoderTicks) {
        if (this.leftDistance == this.rightDistance) {
            return Math.abs(this.leftDistance * 2.0 - Robot.drivetrainSubsystem.getLeftMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP)) < encoderTicks * 2.0;
        } else {
            return Math.abs(this.leftDistance - Robot.drivetrainSubsystem.getLeftMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP)) < encoderTicks
                    && Math.abs(this.rightDistance - Robot.drivetrainSubsystem.getRightMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP)) < encoderTicks;
        }
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

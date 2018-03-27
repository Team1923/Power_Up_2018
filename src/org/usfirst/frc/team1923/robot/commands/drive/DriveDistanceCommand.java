package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

/**
 * Drive the robot for a distance (in inches) using TalonSRX Motion Magic.
 */
public class DriveDistanceCommand extends Command {

    private double distance;

    private double velocity;
    private double acceleration;

    public DriveDistanceCommand(double distance) {
        this(distance, RobotMap.Drivetrain.MM_MAX_VELOCITY, RobotMap.Drivetrain.MM_MAX_ACCELERATION, Integer.MAX_VALUE);
    }

    public DriveDistanceCommand(double distance, double velocity, double acceleration, double timeout) {
        this.requires(Robot.drivetrainSubsystem);
        this.setTimeout(timeout);

        this.distance = Converter.inchesToTicks(distance, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.configureDriving();

        Robot.drivetrainSubsystem.getRightMaster().configMotionAcceleration(Converter.inchesToTicks(this.acceleration, RobotMap.Drivetrain.WHEEL_DIAMETER) / 5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
        Robot.drivetrainSubsystem.getRightMaster().configMotionCruiseVelocity(Converter.inchesToTicks(this.velocity, RobotMap.Drivetrain.WHEEL_DIAMETER) / 5, RobotMap.Robot.CTRE_COMMAND_TIMEOUT_MS);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionMagic, this.distance * 2.0, DemandType.AuxPID, 0.0);
    }

    @Override
    protected boolean isFinished() {
        // return false;

        return this.isTimedOut() || this.isAlmostFinished(RobotMap.Drivetrain.MM_ALLOWABLE_ERROR);
    }

    public boolean isAlmostFinished(int encoderTicks) {
        return Math.abs(this.distance * 2.0 - Robot.drivetrainSubsystem.getRightMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP)) < encoderTicks;
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

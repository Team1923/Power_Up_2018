package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;

/**
 * Drive the robot for a distance (in inches) using TalonSRX Motion Magic.
 */
public class DriveDistanceCommand extends Command {

    private double leftTarget;
    private double rightTarget;

    public DriveDistanceCommand(double distance) {
        this(distance, distance);
    }

    public DriveDistanceCommand(double leftTarget, double rightTarget) {
        this.requires(Robot.drivetrainSubsystem);

        this.leftTarget = DrivetrainSubsystem.distanceToRotations(leftTarget) * RobotMap.Robot.ENCODER_TICKS_PER_ROTATION;
        this.rightTarget = DrivetrainSubsystem.distanceToRotations(rightTarget) * RobotMap.Robot.ENCODER_TICKS_PER_ROTATION;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetPosition();

        Timer.delay(0.10);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive(ControlMode.MotionMagic, this.leftTarget, this.rightTarget);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(this.leftTarget - Robot.drivetrainSubsystem.getLeftEncoderPosition()) < RobotMap.Drivetrain.ALLOWABLE_ERROR ||
                Math.abs(this.rightTarget - Robot.drivetrainSubsystem.getRightEncoderPosition()) < RobotMap.Drivetrain.ALLOWABLE_ERROR;
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        Robot.drivetrainSubsystem.stop();
    }

}

package org.usfirst.frc.team1923.robot.commands.calibrate;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;

/**
 * Command to empirically determine the robot's effective wheelbase width
 */
public class CalibrateWheelbaseCommand extends Command {

    private boolean invert;

    public CalibrateWheelbaseCommand(boolean invert) {
        this.invert = invert;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive((this.invert ? -1 : 1) * 0.4, (this.invert ? -1 : 1) * -0.4);

        double effectiveWheelbaseL = (360.0 * Converter.ticksToInches(Math.abs(Robot.drivetrainSubsystem.getLeftEncoderPosition()), RobotMap.Drivetrain.WHEEL_DIAMETER)) / (Math.PI * Math.abs(Robot.drivetrainSubsystem.getHeading()));
        double effectiveWheelbaseR = (360.0 * Converter.ticksToInches(Math.abs(Robot.drivetrainSubsystem.getRightEncoderPosition()), RobotMap.Drivetrain.WHEEL_DIAMETER)) / (Math.PI * Math.abs(Robot.drivetrainSubsystem.getHeading()));

        System.out.println("Avg: " + round((effectiveWheelbaseL + effectiveWheelbaseR) / 2.0) + ", L: " + round(effectiveWheelbaseL) + ", R: " + round(effectiveWheelbaseR));
    }

    private double round(double in) {
        return Math.round(in * 1000.0) / 1000.0;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

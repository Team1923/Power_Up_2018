package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;
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

        this.leftTarget = DrivetrainSubsystem.distanceToRotations(leftTarget) * 4096;
        this.rightTarget = DrivetrainSubsystem.distanceToRotations(rightTarget) * 4096;
    }

    @Override
    protected void initialize() {
    	System.out.println("init");
    	
    	try {
    		Robot.drivetrainSubsystem.resetPosition();
    		
    		Thread.sleep(250);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @Override
    protected void execute() {
    	Robot.drivetrainSubsystem.drive(ControlMode.MotionMagic, this.leftTarget, this.rightTarget);
    	
        System.out.println("LCE: " + Robot.drivetrainSubsystem.getLeftError() + "\tRCE:" + Robot.drivetrainSubsystem.getRightError() + "\tLCE-RCE: " + (Robot.drivetrainSubsystem.getLeftError() - Robot.drivetrainSubsystem.getRightError())
                + "\tLTE: " + (this.leftTarget - Robot.drivetrainSubsystem.getLeftPosition()) + "\tRTE: " + (this.rightTarget - Robot.drivetrainSubsystem.getRightPosition())
        );
    }

    @Override
    protected boolean isFinished() {
    	if (Robot.oi.driver.circle.get()) {
    		return false;
    	}
    	
        return Math.abs(this.leftTarget - Robot.drivetrainSubsystem.getLeftPosition()) < 300 ||
        		Math.abs(this.rightTarget - Robot.drivetrainSubsystem.getRightEncoderPosition()) < 300;
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

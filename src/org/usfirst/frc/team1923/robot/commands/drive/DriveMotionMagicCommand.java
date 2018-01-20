package org.usfirst.frc.team1923.robot.commands.drive;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Warning: Command not fully functional.
 */
public class DriveMotionMagicCommand extends Command {

    private double dist;
    private double target;
    private int targetEncoderTick;

    public DriveMotionMagicCommand(double dist) {
        this.dist = dist;
        requires(Robot.driveSubSys);
        SmartDashboard.putNumber("Dist to run", dist);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        this.dist = SmartDashboard.getNumber("Dist to run", 0);
        Robot.driveSubSys.configMM();
        Robot.driveSubSys.resetPosition();

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        target = DrivetrainSubsystem.distanceToRotations(this.dist);
        this.targetEncoderTick = (int) (target * 4000);
        System.out.println(target);
        Robot.driveSubSys.drive(target, target);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        System.out.println("Left Error: " + Robot.driveSubSys.getLeftError() + ", Right Error: " + Robot.driveSubSys.getRightError() + ", Tick: "
                + this.targetEncoderTick + ", Left: " + Robot.driveSubSys.getLeftEncPosition() + ", Right: "
                + Robot.driveSubSys.getRightEncPosition());

        Robot.driveSubSys.drive(target, target);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (Math.abs(Robot.driveSubSys.getLeftEncPosition() - this.targetEncoderTick) < 300
                && Math.abs(Robot.driveSubSys.getRightEncPosition() - this.targetEncoderTick) < 300);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        System.out.println(Robot.driveSubSys.getLeftPosition());
        Robot.driveSubSys.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        this.end();
    }
}

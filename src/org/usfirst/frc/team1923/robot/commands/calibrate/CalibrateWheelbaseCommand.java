package org.usfirst.frc.team1923.robot.commands.calibrate;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

/**
 * Command to empirically determine the robot's effective wheelbase width
 */
public class CalibrateWheelbaseCommand extends Command {

    private boolean invert;

    private MotionProfileStatus leftMPStatus;
    private MotionProfileStatus rightMPStatus;

    private double leftTarget;
    private double rightTarget;

    private final double VELOCITY = 1; // Feet / Second

    public CalibrateWheelbaseCommand(boolean invert) {
        this.invert = invert;

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.stop();
        Robot.drivetrainSubsystem.getLeftMasterTalon().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getLeftMasterTalon().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getRightMasterTalon().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getRightMasterTalon().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.setMotionProfile(SetValueMotionProfile.Disable);

        this.pushTrajectoryPoints(64);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive((this.invert ? -1 : 1) * 0.25, (this.invert ? -1 : 1) * -0.25);

        double effectiveWheelbaseL = (360.0 * Converter.ticksToInches(Math.abs(Robot.drivetrainSubsystem.getLeftEncoderPosition()), RobotMap.Drivetrain.WHEEL_DIAMETER)) / (Math.PI * Math.abs(Robot.drivetrainSubsystem.getHeading()));
        double effectiveWheelbaseR = (360.0 * Converter.ticksToInches(Math.abs(Robot.drivetrainSubsystem.getRightEncoderPosition()), RobotMap.Drivetrain.WHEEL_DIAMETER)) / (Math.PI * Math.abs(Robot.drivetrainSubsystem.getHeading()));

        if (this.leftMPStatus.btmBufferCnt < 32 || this.rightMPStatus.btmBufferCnt < 32) {
            this.pushTrajectoryPoints(64);
        }

        System.out.println("Avg: " + round((effectiveWheelbaseL + effectiveWheelbaseR) / 2.0) + ", L: " + round(effectiveWheelbaseL) + ", R: " + round(effectiveWheelbaseR));
    }

    private double round(double in) {
        return Math.round(in * 1000) / 1000;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private void pushTrajectoryPoints(double count) {
        for (int i = 0; i < count; i++) {
            double velocity = Converter.inchesToTicks(Converter.feetToInches(VELOCITY), RobotMap.Drivetrain.WHEEL_DIAMETER);
            this.pushTrajectoryPoint(this.leftTarget, velocity, Robot.drivetrainSubsystem.getLeftMasterTalon());
            this.pushTrajectoryPoint(this.rightTarget, velocity, Robot.drivetrainSubsystem.getRightMasterTalon());

            this.leftTarget += velocity * 0.02;
            this.rightTarget += velocity * 0.02;
        }
    }

    private void pushTrajectoryPoint(double target, double velocity, TalonSRX talon) {
        TrajectoryPoint point = new TrajectoryPoint();

        point.position = target;
        point.profileSlotSelect0 = PIDF.TALON_MOTIONPROFILE_SLOT;
        point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
        point.velocity = velocity;

        talon.pushMotionProfileTrajectory(point);
    }

}

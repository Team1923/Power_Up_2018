package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;


public class TurnGyroCommand extends Command {

    private Notifier notifier;

    private double degrees;
    private double velocity;
    private double acceleration;

    private boolean enabled;

    private MotionProfileStatus leftMPStatus;
    private MotionProfileStatus rightMPStatus;

    public TurnGyroCommand(double degrees) {
        this(degrees, RobotMap.Drivetrain.MMT_MAX_VELOCITY, RobotMap.Drivetrain.MMT_MAX_ACCELERATION);
    }

    public TurnGyroCommand(double degrees, double velocity, double acceleration) {
        this.requires(Robot.drivetrainSubsystem);

        this.degrees = degrees;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        // Robot.drivetrainSubsystem.configureTurning();
        Robot.drivetrainSubsystem.getLeftMaster().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getRightMaster().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getLeftMaster().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getRightMaster().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Disable.value);
        Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Disable.value);

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();

        this.leftMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.leftMPStatus.isLast = false;
        this.rightMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.rightMPStatus.isLast = false;

        this.pushTrajectory(this.degrees, this.velocity, this.acceleration);

        this.enabled = false;
        this.notifier = new Notifier(() -> {
            Robot.drivetrainSubsystem.getLeftMaster().processMotionProfileBuffer();
            Robot.drivetrainSubsystem.getRightMaster().processMotionProfileBuffer();
        });
        this.notifier.startPeriodic(0.0025);
    }

    @Override
    protected void execute() {
        if (this.leftMPStatus.isLast && this.rightMPStatus.isLast) {
            Robot.drivetrainSubsystem.stop();
        }

        Robot.drivetrainSubsystem.getLeftMaster().getMotionProfileStatus(this.leftMPStatus);
        Robot.drivetrainSubsystem.getRightMaster().getMotionProfileStatus(this.rightMPStatus);

        if (!this.enabled && this.leftMPStatus.btmBufferCnt > 10 && this.rightMPStatus.btmBufferCnt > 10) {
            Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Enable.value);
            Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Enable.value);

            this.enabled = true;
        }
    }

    @Override
    protected void end() {
        this.notifier.stop();
        this.notifier = null;

        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return this.leftMPStatus.isLast && this.rightMPStatus.isLast && this.enabled;
    }

    public boolean isAlmostFinished(double headingError) {
        return false;
    }

    private void pushTrajectory(double target, double velocity, double acceleration) {
        acceleration /= 10;

        boolean invert = target < 0;
        target = Math.abs(target * 10);

        int steps = (int) Math.round(velocity < Math.sqrt(acceleration * target) ? target / velocity + velocity / acceleration : 2 * Math.sqrt(target / acceleration));
        double cutoff = Math.min(velocity / acceleration, steps / 2);

        TrajectoryPoint point = new TrajectoryPoint();

        point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
        point.headingDeg = 0;
        point.position = 0;
        point.profileSlotSelect0 = PIDF.TALON_MOTIONPROFILE_SLOT;
        point.profileSlotSelect1 = PIDF.TALON_TURN_SLOT;

        for (int i = 0; i < 5 * steps; ++i) {
            if ((i / 5.0) <= cutoff) {
                point.velocity = acceleration * (i / 5.0);
                point.auxiliaryPos = acceleration * (i / 5.0) * (i / 5.0) / 2;
            } else if ((i / 5.0) >= steps - cutoff) {
                point.velocity = acceleration * (steps - (i / 5.0));
                point.auxiliaryPos = target - acceleration * Math.pow(steps - (i / 5.0), 2) / 2;
            } else {
                point.velocity = velocity;
                point.auxiliaryPos = acceleration * cutoff * cutoff / 2 + velocity * ((i / 5.0) - cutoff);
            }

            if (invert) {
                point.velocity *= -1;
                point.auxiliaryPos *= -1;
            }

            point.velocity *= 10;
            point.isLastPoint = i == 5 * steps - 1;

            point.velocity = Converter.inchesToTicks(
                    point.velocity * Math.PI * RobotMap.Drivetrain.WHEELBASE_WIDTH / 3600 / 4.15,
                    RobotMap.Drivetrain.WHEEL_DIAMETER
            );

            Robot.drivetrainSubsystem.getRightMaster().pushMotionProfileTrajectory(point);

            point.velocity *= -1.0;

            Robot.drivetrainSubsystem.getLeftMaster().pushMotionProfileTrajectory(point);
        }
    }

}

package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import jaci.pathfinder.Trajectory;

import jaci.pathfinder.modifiers.TankModifier;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryUtils;
import org.usfirst.frc.team1923.robot.utils.pathfinder.modifier.PathModifier;

public class DriveTrajectoryCommand extends Command {

    private TrajectoryPoint[] leftTrajectory;
    private TrajectoryPoint[] rightTrajectory;

    private MotionProfileStatus leftMPStatus;
    private MotionProfileStatus rightMPStatus;

    private Notifier notifier;

    private double startTime;

    private boolean enabled;

    public DriveTrajectoryCommand(TrajectoryStore.Path path) {
        this(TrajectoryStore.loadTrajectory(path), path.getVelocityMultiplier());
    }

    public DriveTrajectoryCommand(Trajectory trajectory, TrajectoryStore.VelocityMultiplier velocityMultiplier) {
        this.requires(Robot.drivetrainSubsystem);
        this.setInterruptible(false);

        this.leftTrajectory = new TrajectoryPoint[trajectory.segments.length];
        this.rightTrajectory = new TrajectoryPoint[trajectory.segments.length];

        TankModifier modifier = new TankModifier(trajectory).modify(Converter.inchesToFeet(RobotMap.Drivetrain.WHEEL_DIAMETER));

        Trajectory leftTrajectory = TrajectoryUtils.correctHeadings(modifier.getLeftTrajectory());
        Trajectory rightTrajectory = TrajectoryUtils.correctHeadings(modifier.getRightTrajectory());

        if (leftTrajectory.segments.length != rightTrajectory.segments.length) {
            throw new RuntimeException("Trajectory segments not in sync.");
        }

        for (int i = 0; i < trajectory.segments.length; i++) {
            Trajectory.Segment leftSegment = leftTrajectory.segments[i];
            Trajectory.Segment rightSegment = rightTrajectory.segments[i];

            double velocityCorrection = (rightSegment.velocity - leftSegment.velocity) * velocityMultiplier.getConstant(i);

            this.leftTrajectory[i] = new TrajectoryPoint();
            this.rightTrajectory[i] = new TrajectoryPoint();

            this.leftTrajectory[i].position = this.rightTrajectory[i].position = Converter.inchesToTicks(Converter.feetToInches(leftSegment.position + rightSegment.position), RobotMap.Drivetrain.WHEEL_DIAMETER);
            this.leftTrajectory[i].auxiliaryPos = this.rightTrajectory[i].auxiliaryPos = 10.0 * Math.toDegrees(leftSegment.heading);
            this.leftTrajectory[i].profileSlotSelect0 = this.rightTrajectory[i].profileSlotSelect0 = PIDF.TALON_MOTIONPROFILE_SLOT;
            this.leftTrajectory[i].profileSlotSelect1 = this.rightTrajectory[i].profileSlotSelect1 = PIDF.TALON_GYRO_SLOT;
            this.leftTrajectory[i].timeDur = this.rightTrajectory[i].timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
            this.leftTrajectory[i].zeroPos = this.rightTrajectory[i].zeroPos = i == 0;
            this.leftTrajectory[i].isLastPoint = this.rightTrajectory[i].isLastPoint = i == trajectory.segments.length - 1;

            this.leftTrajectory[i].velocity = Converter.inchesToTicks(Converter.feetToInches(leftSegment.velocity - velocityCorrection), RobotMap.Drivetrain.WHEEL_DIAMETER) / 10.0;
            this.rightTrajectory[i].velocity = Converter.inchesToTicks(Converter.feetToInches(rightSegment.velocity + velocityCorrection), RobotMap.Drivetrain.WHEEL_DIAMETER) / 10.0;
        }
    }

    public DriveTrajectoryCommand modify(PathModifier modifier) {
        this.leftTrajectory = modifier.modifyPath(this.leftTrajectory);
        this.rightTrajectory = modifier.modifyPath(this.rightTrajectory);

        return this;
    }

    public DriveTrajectoryCommand modifyLeft(PathModifier modifier) {
        this.leftTrajectory = modifier.modifyPath(this.leftTrajectory);

        return this;
    }

    public DriveTrajectoryCommand modifyRight(PathModifier modifier) {
        this.rightTrajectory = modifier.modifyPath(this.rightTrajectory);

        return this;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.configureDriving();
        Robot.drivetrainSubsystem.getLeftMaster().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getRightMaster().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getLeftMaster().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getRightMaster().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Disable.value);
        Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Disable.value);

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();

        this.leftMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.rightMPStatus.topBufferCnt = Integer.MAX_VALUE;

        for (TrajectoryPoint point : this.leftTrajectory) {
            Robot.drivetrainSubsystem.getLeftMaster().pushMotionProfileTrajectory(point);
        }

        for (TrajectoryPoint point : this.rightTrajectory) {
            Robot.drivetrainSubsystem.getRightMaster().pushMotionProfileTrajectory(point);
        }

        this.enabled = false;
        this.notifier = new Notifier(() -> {
            Robot.drivetrainSubsystem.getLeftMaster().processMotionProfileBuffer();
            Robot.drivetrainSubsystem.getRightMaster().processMotionProfileBuffer();
        });
        this.notifier.startPeriodic(0.0025);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.getLeftMaster().getMotionProfileStatus(this.leftMPStatus);
        Robot.drivetrainSubsystem.getRightMaster().getMotionProfileStatus(this.rightMPStatus);

        if (!this.enabled && this.leftMPStatus.btmBufferCnt > 15 && this.rightMPStatus.btmBufferCnt > 15) {
            Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Enable.value);
            Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Enable.value);

            this.enabled = true;
            this.startTime = Timer.getFPGATimestamp();
        }

        String headingError = "NaN";

        if (this.enabled) {
            int currentIndex = this.leftTrajectory.length - this.leftMPStatus.btmBufferCnt - this.leftMPStatus.topBufferCnt - 1;

            if (currentIndex >= 0 && currentIndex < this.leftTrajectory.length) {
                headingError = (this.leftTrajectory[currentIndex].auxiliaryPos / 10.0 - Robot.drivetrainSubsystem.getHeading()) + "";
            }
        }

        System.out.println("Points Left: " + (this.leftMPStatus.topBufferCnt + this.leftMPStatus.btmBufferCnt) + ", Status: " + this.leftMPStatus.outputEnable.name() + ":" + this.rightMPStatus.outputEnable.name()
                + ", LO: " + Robot.drivetrainSubsystem.getLeftMaster().getMotorOutputPercent()
                + ", RO: " + Robot.drivetrainSubsystem.getRightMaster().getMotorOutputPercent()
                + ", Underrun: " + this.leftMPStatus.hasUnderrun + ":" + this.rightMPStatus.hasUnderrun
                + ", Heading Err: " + headingError
        );

        if (this.leftMPStatus.isLast && this.enabled && this.leftMPStatus.outputEnable == SetValueMotionProfile.Enable) {
            Robot.drivetrainSubsystem.getLeftMaster().neutralOutput();
            Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.PercentOutput, 0);

            System.out.println("Left MP (" + this.leftTrajectory.length + " points) completed in " + (Timer.getFPGATimestamp() - this.startTime) + "s.");
        }

        if (this.rightMPStatus.isLast && this.enabled && this.rightMPStatus.outputEnable == SetValueMotionProfile.Enable) {
            Robot.drivetrainSubsystem.getRightMaster().neutralOutput();
            Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.PercentOutput, 0);

            System.out.println("Right MP (" + this.rightTrajectory.length + " points) completed in " + (Timer.getFPGATimestamp() - this.startTime) + "s.");
        }
    }

    @Override
    protected boolean isFinished() {
        return this.leftMPStatus.isLast && this.rightMPStatus.isLast && this.enabled;
    }

    public boolean isAlmostFinished(int segmentsRemaining) {
        return this.leftMPStatus.btmBufferCnt + this.leftMPStatus.topBufferCnt < segmentsRemaining && this.rightMPStatus.btmBufferCnt + this.rightMPStatus.topBufferCnt < segmentsRemaining;
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

}

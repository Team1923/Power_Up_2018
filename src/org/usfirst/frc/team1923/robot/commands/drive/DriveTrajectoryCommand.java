package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import jaci.pathfinder.Trajectory;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryUtils;

public class DriveTrajectoryCommand extends Command {

    private Trajectory trajectory;
    private MotionProfileStatus mpStatus;

    private boolean enabled;

    public DriveTrajectoryCommand(TrajectoryStore.Path path) {
        this(TrajectoryStore.loadTrajectory(path));
    }

    public DriveTrajectoryCommand(Trajectory trajectory) {
        this.requires(Robot.drivetrainSubsystem);
        this.setInterruptible(false);
        
        this.trajectory = TrajectoryUtils.correctHeadings(trajectory);
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.configureDriving();
        Robot.drivetrainSubsystem.getRightMaster().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getRightMaster().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Disable.value);

        this.mpStatus = new MotionProfileStatus();
        this.mpStatus.topBufferCnt = Integer.MAX_VALUE;

        for (int i = 0; i < this.trajectory.segments.length; i++) {
            TrajectoryPoint point = new TrajectoryPoint();

            point.position = 2 * Converter.inchesToTicks(Converter.feetToInches(this.trajectory.segments[i].position), RobotMap.Drivetrain.WHEEL_DIAMETER);
            point.auxiliaryPos = 10.0 * Math.toDegrees(this.trajectory.segments[i].heading);
            point.profileSlotSelect0 = PIDF.TALON_MOTIONPROFILE_SLOT;
            point.profileSlotSelect1 = PIDF.TALON_GYRO_SLOT;
            point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
            point.velocity = Converter.inchesToTicks(Converter.feetToInches(this.trajectory.segments[i].velocity), RobotMap.Drivetrain.WHEEL_DIAMETER) / 5.0;
            point.zeroPos = i == 0;
            point.isLastPoint = i == this.trajectory.segments.length - 1;

            Robot.drivetrainSubsystem.getRightMaster().pushMotionProfileTrajectory(point);
        }

        this.enabled = false;
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.getRightMaster().getMotionProfileStatus(this.mpStatus);

        if (!this.enabled && this.mpStatus.btmBufferCnt > 25) {
            Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionProfileArc, SetValueMotionProfile.Enable.value);
            this.enabled = true;
        }

        String headingError = "NaN";

        if (this.enabled) {
            int currentIndex = this.trajectory.segments.length - this.mpStatus.btmBufferCnt - this.mpStatus.topBufferCnt;

            if (currentIndex >= 0) {
                headingError = (Math.toDegrees(this.trajectory.segments[currentIndex].heading) - Robot.drivetrainSubsystem.getHeading()) + "";
            }
        }

        System.out.println("Points Left: " + (this.mpStatus.topBufferCnt + this.mpStatus.btmBufferCnt) + ", Status: " + this.mpStatus.outputEnable.name()
                + ", LO: " + Robot.drivetrainSubsystem.getLeftMaster().getMotorOutputPercent()
                + ", RO: " + Robot.drivetrainSubsystem.getRightMaster().getMotorOutputPercent()
                + ", Underrun: " + this.mpStatus.hasUnderrun
                + ", Heading Err: " + headingError
        );
    }

    @Override
    protected boolean isFinished() {
        if (Robot.oi.driver.circle.get()) {
            return false;
        }

        return this.mpStatus.isLast;
    }

    public boolean isAlmostFinished(int segmentsRemaining) {
        return this.mpStatus.btmBufferCnt + this.mpStatus.topBufferCnt < segmentsRemaining;
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

package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class DriveTrajectoryCommand extends Command {

    private Trajectory trajectory;

    private MotionProfileStatus leftMPStatus;
    private MotionProfileStatus rightMPStatus;

    private Trajectory leftTrajectory;
    private Trajectory rightTrajectory;

    public DriveTrajectoryCommand(TrajectoryStore.Path path) {
        this(TrajectoryStore.loadTrajectory(path));
    }

    public DriveTrajectoryCommand(Trajectory trajectory) {
        this.requires(Robot.drivetrainSubsystem);
        
        this.trajectory = trajectory;

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();

        this.leftMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.rightMPStatus.btmBufferCnt = Integer.MAX_VALUE;

        this.setInterruptible(false);
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.stop();
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        Robot.drivetrainSubsystem.getLeftMasterTalon().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getLeftMasterTalon().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getRightMasterTalon().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getRightMasterTalon().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.setMotionProfile(SetValueMotionProfile.Disable);

        TankModifier modifier = new TankModifier(this.trajectory).modify(Converter.inchesToFeet(RobotMap.Drivetrain.WHEELBASE_WIDTH));
        this.leftTrajectory = modifier.getLeftTrajectory();
        this.rightTrajectory = modifier.getRightTrajectory();

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();
        this.leftMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.rightMPStatus.btmBufferCnt = Integer.MAX_VALUE;

        this.pushTrajectoryPoints();

        Robot.drivetrainSubsystem.setMotionProfile(SetValueMotionProfile.Enable);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.getLeftMasterTalon().getMotionProfileStatus(this.leftMPStatus);
        Robot.drivetrainSubsystem.getRightMasterTalon().getMotionProfileStatus(this.rightMPStatus);
    }

    @Override
    protected boolean isFinished() {
        return this.leftMPStatus.isLast && this.rightMPStatus.isLast;
    }

    public boolean isAlmostFinished(int segmentsRemaining) {
        return this.leftMPStatus.btmBufferCnt + this.leftMPStatus.topBufferCnt < segmentsRemaining && this.rightMPStatus.btmBufferCnt + this.rightMPStatus.topBufferCnt < segmentsRemaining;
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    private void pushTrajectoryPoints() {
        for (int i = 0; i < this.leftTrajectory.segments.length; i++) {
            this.pushTrajectoryPoint(
                    this.leftTrajectory.segments[i],
                    Robot.drivetrainSubsystem.getLeftMasterTalon(),
                    i == 0,
                    i == this.leftTrajectory.segments.length - 1
            );
        }

        for (int i = 0; i < this.rightTrajectory.segments.length; i++) {
            this.pushTrajectoryPoint(
                    this.rightTrajectory.segments[i],
                    Robot.drivetrainSubsystem.getRightMasterTalon(),
                    i == 0,
                    i == this.rightTrajectory.segments.length - 1
            );
        }
    }

    private void pushTrajectoryPoint(Trajectory.Segment segment, TalonSRX talon, boolean isFirst, boolean isLast) {
        TrajectoryPoint point = new TrajectoryPoint();

        point.position = Converter.inchesToTicks(Converter.feetToInches(segment.position), RobotMap.Drivetrain.WHEEL_DIAMETER);
        point.auxiliaryPos = Pathfinder.boundHalfDegrees(Pathfinder.r2d(segment.heading)) * 10.0;
        point.headingDeg = Pathfinder.r2d(segment.heading);
        point.profileSlotSelect0 = PIDF.TALON_MOTIONPROFILE_SLOT;
        point.profileSlotSelect1 = PIDF.TALON_GYRO_SLOT;
        point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
        point.velocity = Converter.inchesToTicks(Converter.feetToInches(segment.velocity), RobotMap.Drivetrain.WHEEL_DIAMETER) / 10.0;
        point.zeroPos = isFirst;
        point.isLastPoint = isLast;

        talon.pushMotionProfileTrajectory(point);
    }

}

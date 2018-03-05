package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Command;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.logger.TransientDataSource;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class DriveTrajectoryCommand extends Command {

    private Trajectory trajectory;

    private MotionProfileStatus leftMPStatus;
    private MotionProfileStatus rightMPStatus;

    private Trajectory leftTrajectory;
    private Trajectory rightTrajectory;

    private int leftTarget;
    private int rightTarget;

    public DriveTrajectoryCommand(TrajectoryStore.Waypoints waypoints) {
        this(TrajectoryStore.loadTrajectory(waypoints));
    }

    public DriveTrajectoryCommand(Trajectory trajectory) {
        this.requires(Robot.drivetrainSubsystem);
        
        this.trajectory = trajectory;

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();

        this.leftMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.rightMPStatus.btmBufferCnt = Integer.MAX_VALUE;

//        Robot.logger.addTransientDataSource("DriveTrajectoryCommand_LeftError", new TransientDataSource(
//                () -> (
//                        Converter.inchesToTicks(
//                                Converter.feetToInches(this.leftTrajectory.segments[this.leftTrajectory.segments.length - this.leftMPStatus.topBufferCnt - this.leftMPStatus.btmBufferCnt - 1].position),
//                                RobotMap.Drivetrain.WHEEL_DIAMETER
//                        ) - Robot.drivetrainSubsystem.getLeftEncoderPosition()
//                ) + "",
//                this::isRunning
//        ));
//
//        Robot.logger.addTransientDataSource("DriveTrajectoryCommand_RightError", new TransientDataSource(
//                () -> (
//                        Converter.inchesToTicks(
//                                Converter.feetToInches(this.rightTrajectory.segments[this.rightTrajectory.segments.length - this.rightMPStatus.topBufferCnt - this.rightMPStatus.btmBufferCnt - 1].position),
//                                RobotMap.Drivetrain.WHEEL_DIAMETER
//                        ) - Robot.drivetrainSubsystem.getLeftEncoderPosition()
//                ) + "",
//                this::isRunning
//        ));
    }

    @Override
    protected void initialize() {
        this.resetTalons();

        Robot.drivetrainSubsystem.resetPosition();

        TankModifier modifier = new TankModifier(this.trajectory).modify(Converter.inchesToFeet(RobotMap.Drivetrain.WHEELBASE_WIDTH));

        this.leftMPStatus = new MotionProfileStatus();
        this.rightMPStatus = new MotionProfileStatus();

        this.leftMPStatus.topBufferCnt = Integer.MAX_VALUE;
        this.rightMPStatus.btmBufferCnt = Integer.MAX_VALUE;

        this.leftTrajectory = modifier.getLeftTrajectory();
        this.rightTrajectory = modifier.getRightTrajectory();

        this.leftTarget = Converter.inchesToTicks(Converter.feetToInches(this.leftTrajectory.segments[this.leftTrajectory.segments.length - 1].position), RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.rightTarget = Converter.inchesToTicks(Converter.feetToInches(this.rightTrajectory.segments[this.rightTrajectory.segments.length - 1].position), RobotMap.Drivetrain.WHEEL_DIAMETER);

        Robot.drivetrainSubsystem.setMotionProfile(SetValueMotionProfile.Disable);

        this.pushTrajectoryPoints();

        Robot.drivetrainSubsystem.setMotionProfile(SetValueMotionProfile.Enable);

        System.out.println("DriveTrajectoryCommand Init @ " + System.currentTimeMillis());
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.getLeftMasterTalon().getMotionProfileStatus(this.leftMPStatus);
        Robot.drivetrainSubsystem.getRightMasterTalon().getMotionProfileStatus(this.rightMPStatus);

        System.out.println("Left Segment: " + this.getLeftSegmentId() + ", Right Segment: " + this.getRightSegmentId());
    }

    @Override
    protected boolean isFinished() {
        if (Robot.oi.driver.cross.get()) {
            return false;
        }

        return this.leftMPStatus.isLast && this.rightMPStatus.isLast;
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.setMotionProfile(SetValueMotionProfile.Disable);
        Robot.drivetrainSubsystem.stop();

        System.out.println("DriveTrajectoryCommand End @ " + System.currentTimeMillis());
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    private void resetTalons() {
        System.out.println("RESET");
        Robot.drivetrainSubsystem.drive(ControlMode.PercentOutput, 0, 0);

        Robot.drivetrainSubsystem.getLeftMasterTalon().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getLeftMasterTalon().clearMotionProfileHasUnderrun(0);
        Robot.drivetrainSubsystem.getRightMasterTalon().clearMotionProfileTrajectories();
        Robot.drivetrainSubsystem.getRightMasterTalon().clearMotionProfileHasUnderrun(0);
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

        point.headingDeg = 0;
        point.isLastPoint = isLast;
        point.position = Converter.inchesToTicks(Converter.feetToInches(segment.position), RobotMap.Drivetrain.WHEEL_DIAMETER);
        point.profileSlotSelect0 = 1;
        point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
        point.velocity = Converter.inchesToTicks(Converter.feetToInches(segment.velocity), RobotMap.Drivetrain.WHEEL_DIAMETER);
        point.zeroPos = isFirst;

        talon.pushMotionProfileTrajectory(point);
    }

    public int getLeftSegmentId() {
        return this.leftTrajectory.segments.length - this.leftMPStatus.btmBufferCnt - this.leftMPStatus.topBufferCnt - 1;
    }

    public int getRightSegmentId() {
        return this.rightTrajectory.segments.length - this.rightMPStatus.btmBufferCnt - this.rightMPStatus.topBufferCnt - 1;
    }

}

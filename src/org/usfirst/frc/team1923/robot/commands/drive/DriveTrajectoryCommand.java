package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.pathfinder.EncoderFollower;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class DriveTrajectoryCommand extends Command {

    private Trajectory trajectory;

    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;

    private double lastTime;

    public DriveTrajectoryCommand(TrajectoryStore.Waypoints waypoints) {
        this(TrajectoryStore.loadTrajectory(waypoints));
    }
    
    public DriveTrajectoryCommand(Trajectory trajectory) {
        this.requires(Robot.drivetrainSubsystem);
        
        this.trajectory = trajectory;

        SmartDashboard.putNumber("K_P", RobotMap.Drivetrain.TRAJECTORY_P);
        SmartDashboard.putNumber("K_I", RobotMap.Drivetrain.TRAJECTORY_I);
        SmartDashboard.putNumber("K_D", RobotMap.Drivetrain.TRAJECTORY_D);
        SmartDashboard.putNumber("K_V", RobotMap.Drivetrain.TRAJECTORY_VELOCITY_CONSTANT);
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();

        Timer.delay(0.1);
        
        TankModifier modifier = new TankModifier(this.trajectory).modify(Converter.inchesToMeters(RobotMap.Drivetrain.WHEELBASE_WIDTH));

        this.leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
        this.leftFollower.configureEncoder(Robot.drivetrainSubsystem.getLeftEncoderPosition(), RobotMap.Robot.ENCODER_TICKS_PER_ROTATION, Converter.inchesToMeters(RobotMap.Drivetrain.WHEEL_DIAMETER));
        this.leftFollower.configurePIDVA(
                SmartDashboard.getNumber("K_P", RobotMap.Drivetrain.TRAJECTORY_P),
                SmartDashboard.getNumber("K_I", RobotMap.Drivetrain.TRAJECTORY_I),
                SmartDashboard.getNumber("K_D", RobotMap.Drivetrain.TRAJECTORY_D),
                SmartDashboard.getNumber("K_V", RobotMap.Drivetrain.TRAJECTORY_VELOCITY_CONSTANT),
                0
        );

        this.rightFollower = new EncoderFollower(modifier.getRightTrajectory());
        this.rightFollower.configureEncoder(Robot.drivetrainSubsystem.getRightEncoderPosition(), RobotMap.Robot.ENCODER_TICKS_PER_ROTATION, Converter.inchesToMeters(RobotMap.Drivetrain.WHEEL_DIAMETER));
        this.rightFollower.configurePIDVA(
                SmartDashboard.getNumber("K_P", RobotMap.Drivetrain.TRAJECTORY_P),
                SmartDashboard.getNumber("K_I", RobotMap.Drivetrain.TRAJECTORY_I),
                SmartDashboard.getNumber("K_D", RobotMap.Drivetrain.TRAJECTORY_D),
                SmartDashboard.getNumber("K_V", RobotMap.Drivetrain.TRAJECTORY_VELOCITY_CONSTANT),
                0
        );

        this.lastTime = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        double leftOutput = this.leftFollower.calculate(Robot.drivetrainSubsystem.getLeftEncoderPosition());
        double rightOutput = this.rightFollower.calculate(Robot.drivetrainSubsystem.getRightEncoderPosition());

        double headingError = Pathfinder.boundHalfDegrees(Pathfinder.r2d(this.leftFollower.getHeading()) - Robot.drivetrainSubsystem.getHeading());
        double turnOutput = 0.8 * (-1.0 / 80.0) * headingError;
        
        System.out.println("LO: " + leftOutput + ", RO: " + rightOutput + ", TO: " + turnOutput + ", Button State: " + Robot.oi.driver.circle.get() + ", LE: " + this.leftFollower.last_error + ", RE: " + this.rightFollower.last_error + ", DT: " + (Timer.getFPGATimestamp() - this.lastTime));
        this.lastTime = Timer.getFPGATimestamp();
        // turnOutput = 0;

        Robot.drivetrainSubsystem.drive(ControlMode.PercentOutput, leftOutput + turnOutput, rightOutput - turnOutput);
    }

    @Override
    protected boolean isFinished() {
        return this.leftFollower.isFinished() && this.rightFollower.isFinished() && !Robot.oi.driver.circle.get();
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

package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.usfirst.frc.team1923.robot.Measurement;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class DriveTrajectoryCommand extends Command {

    private Trajectory trajectory;

    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;

    public DriveTrajectoryCommand(TrajectoryStore.Waypoints waypoints) {
        this(TrajectoryStore.loadTrajectory(waypoints));
    }
    
    public DriveTrajectoryCommand(Trajectory trajectory) {
        this.requires(Robot.drivetrainSubsystem);
        
        this.trajectory = trajectory;
        
        System.out.println("Max V: " + TrajectoryStore.trajectoryConfig.max_velocity);
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.resetHeading();
        Robot.drivetrainSubsystem.resetPosition();
        
        try {
            Thread.sleep(250);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        TankModifier modifier = new TankModifier(this.trajectory).modify(Measurement.ROBOT_WHEELBASE_WIDTH.inMeters());

        this.leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
        this.leftFollower.configureEncoder(Robot.drivetrainSubsystem.getLeftEncoderPosition(), 4096, Measurement.ROBOT_WHEEL_DIAMETER.inMeters());
        this.leftFollower.configurePIDVA(0.00, 0, 0, Measurement.DRIVETRAIN_VELOCIY_CONSTANT, 0);

        this.rightFollower = new EncoderFollower(modifier.getRightTrajectory());
        this.rightFollower.configureEncoder(Robot.drivetrainSubsystem.getRightEncoderPosition(), 4096, Measurement.ROBOT_WHEEL_DIAMETER.inMeters());
        this.rightFollower.configurePIDVA(0.00, 0, 0, Measurement.DRIVETRAIN_VELOCIY_CONSTANT, 0);
    }

    @Override
    protected void execute() {
        double leftOutput = this.leftFollower.calculate(Robot.drivetrainSubsystem.getLeftEncoderPosition());
        double rightOutput = this.rightFollower.calculate(Robot.drivetrainSubsystem.getRightEncoderPosition());

        double headingError = Pathfinder.boundHalfDegrees(Pathfinder.r2d(this.leftFollower.getHeading()) - Robot.drivetrainSubsystem.getHeading());
        double turnOutput = 0.8 * (-1.0 / 80.0) * headingError;
        
        System.out.println("LO" + leftOutput + ", RO: " + rightOutput + ", TO: " + turnOutput + ", Button State: " + Robot.oi.driver.circle.get());
        
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

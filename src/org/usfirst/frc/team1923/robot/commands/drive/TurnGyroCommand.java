package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

public class TurnGyroCommand extends Command {

    private double heading;
    private int targetHeading;

    private boolean absolute;

    public TurnGyroCommand(double heading) {
        this(heading, true);
    }

    /**
     * Turn the robot to the specified heading (or by the specified amount) using the PigeonIMU and MotionMagic
     *
     * @param heading Target heading in degrees: left turns are positive, right turns are negative
     * @param absolute Set to true for absolute heading
     */
    public TurnGyroCommand(double heading, boolean absolute) {
        this.heading = heading % 360.0;
        this.absolute = absolute;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.configTalonTurning();

        this.targetHeading = Converter.degreesToPidgeonTicks(Robot.drivetrainSubsystem.getHeading() + this.heading);

        Robot.drivetrainSubsystem.drive(ControlMode.MotionMagic, this.targetHeading, this.targetHeading);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.drive(ControlMode.MotionMagic, this.targetHeading, this.targetHeading);
        System.out.println(Robot.drivetrainSubsystem.getLeftMasterTalon().getClosedLoopError(PIDF.PRIMARY_LOOP));

        System.out.println("Current: " + Robot.drivetrainSubsystem.getHeading() + ", Target: " + this.targetHeading + ", Error: " + Math.abs(Robot.drivetrainSubsystem.getHeading() - this.targetHeading));
        //System.out.println("Sensor Pos: " + Robot.drivetrainSubsystem.getLeftEncoderPosition() + ", " + Robot.drivetrainSubsystem.getRightEncoderPosition());
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
        Robot.drivetrainSubsystem.configTalonDriving();
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    public boolean isAlmostFinished(double headingError) {
        return false;
    }

    private double boundPositiveDegrees(double heading) {
        heading = heading % 360.0;

        while (heading < 0) {
            heading += 360.0;
        }

        return heading;
    }

}

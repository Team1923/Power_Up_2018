package org.usfirst.frc.team1923.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

public class TurnGyroCommand extends Command {

    private double degrees;
    private int target;

    /**
     * Turn the robot by the specified amount using the PigeonIMU and MotionMagic
     *
     * @param degrees Target heading in degrees: left turns are positive, right turns are negative
     */
    public TurnGyroCommand(double degrees) {
        this.degrees = degrees;
    }

    @Override
    protected void initialize() {
        Robot.drivetrainSubsystem.configureTurning();

        this.target = Converter.degreesToPidgeonTicks(Robot.drivetrainSubsystem.getHeading() + this.degrees);
    }

    @Override
    protected void execute() {
        Robot.drivetrainSubsystem.getLeftMaster().set(ControlMode.PercentOutput, 0);
        Robot.drivetrainSubsystem.getRightMaster().set(ControlMode.MotionMagic, this.target);

        System.out.println("Left Output: " + Robot.drivetrainSubsystem.getLeftMaster().getMotorOutputPercent() + ", Right Output: " + Robot.drivetrainSubsystem.getRightMaster().getMotorOutputPercent()
                + ", Left Mode: " + Robot.drivetrainSubsystem.getLeftMaster().getControlMode().name() + ", Right Mode: " + Robot.drivetrainSubsystem.getRightMaster().getControlMode().name()
                + ", CLError: " + Robot.drivetrainSubsystem.getRightMaster().getClosedLoopError(PIDF.PRIMARY_LOOP)
                + ", CLTarget: " + Robot.drivetrainSubsystem.getRightMaster().getClosedLoopTarget(PIDF.PRIMARY_LOOP)
        );
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
        Robot.drivetrainSubsystem.configureDriving();

        System.out.println("END END END");
    }

    @Override
    protected void interrupted() {
        this.end();
    }

    @Override
    protected boolean isFinished() {
        if (Robot.oi.driver.circle.get()) {
            return false;
        }

        return this.isAlmostFinished(RobotMap.Drivetrain.MMT_ALLOWABLE_ERROR);
    }

    public boolean isAlmostFinished(double headingError) {
        return false;
    }

}

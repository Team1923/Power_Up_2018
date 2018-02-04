package org.usfirst.frc.team1923.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.commands.drive.DriveControlCommand;
import org.usfirst.frc.team1923.robot.utils.battery.OutputCallback;
import org.usfirst.frc.team1923.robot.utils.battery.OutputType;

public class DrivetrainSubsystem extends Subsystem {

    private static final double WHEEL_DIAMETER = 6;
    private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

    private TalonSRX[] leftTalons;
    private TalonSRX[] rightTalons;

    private ControlMode controlMode;

    private double leftOutput;
    private double rightOutput;

    public DrivetrainSubsystem() {
        this.leftTalons = new TalonSRX[RobotMap.DRIVE_LEFT_TALON_PORTS.length];
        this.rightTalons = new TalonSRX[RobotMap.DRIVE_RIGHT_TALON_PORTS.length];

        this.controlMode = ControlMode.PercentOutput;

        for (int i = 0; i < this.leftTalons.length; i++) {
            this.leftTalons[i] = new TalonSRX(RobotMap.DRIVE_LEFT_TALON_PORTS[i]);

            if (i > 0) {
                this.leftTalons[i].set(ControlMode.Follower, RobotMap.DRIVE_LEFT_TALON_PORTS[0]);
            }

            this.configureTalon(this.leftTalons[i]);
        }

        for (int i = 0; i < this.rightTalons.length; i++) {
            this.rightTalons[i] = new TalonSRX(RobotMap.DRIVE_RIGHT_TALON_PORTS[i]);

            if (i > 0) {
                this.rightTalons[i].set(ControlMode.Follower, RobotMap.DRIVE_RIGHT_TALON_PORTS[0]);
            }

            this.rightTalons[i].setInverted(true);
            this.configureTalon(this.rightTalons[i]);
        }
    }

    public void drive(double leftOutput, double rightOutput) {
        Robot.batteryMonitor.queueAction(OutputType.MINICIM, leftOutput, new OutputCallback() {

            @Override
            public void callback(double output) {
                DrivetrainSubsystem.this.leftTalons[0].set(DrivetrainSubsystem.this.controlMode, output);
                DrivetrainSubsystem.this.leftOutput = output;
            }

        }, 10);

        Robot.batteryMonitor.queueAction(OutputType.MINICIM, rightOutput, new OutputCallback() {

            @Override
            public void callback(double output) {
                DrivetrainSubsystem.this.rightTalons[0].set(DrivetrainSubsystem.this.controlMode, output);
                DrivetrainSubsystem.this.rightOutput = output;
            }

        }, 10);
    }

    public void setControlMode(ControlMode mode) {
        this.controlMode = mode;

        this.drive(this.leftOutput, this.rightOutput);
    }

    public void resetPosition() {
        this.leftTalons[0].setSelectedSensorPosition(0, 0, RobotMap.TALON_COMMAND_TIMEOUT);
        this.rightTalons[0].setSelectedSensorPosition(0, 0, RobotMap.TALON_COMMAND_TIMEOUT);
    }

    public double getLeftPosition() {
        return this.leftTalons[0].getSelectedSensorPosition(0);
    }

    public double getRightPosition() {
        return this.rightTalons[0].getSelectedSensorPosition(0);
    }

    public int getLeftEncPosition() {
        return this.leftTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public int getRightEncPosition() {
        return this.rightTalons[0].getSensorCollection().getPulseWidthPosition();
    }

    public double getLeftError() {
        return this.leftTalons[0].getClosedLoopError(0);
    }

    public double getRightError() {
        return this.rightTalons[0].getClosedLoopError(0);
    }

    public void stop() {
        drive(0, 0);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveControlCommand());
    }

    private double distanceToRotations(double distance) {
        return distance / WHEEL_CIRCUMFERENCE;
    }

    private double rotationsToDistance(double rotations) {
        return rotations * WHEEL_CIRCUMFERENCE;
    }

    private void configureTalon(TalonSRX talon) {
        talon.configNominalOutputForward(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configNominalOutputReverse(0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputForward(1.0, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configPeakOutputReverse(-1.0, RobotMap.TALON_COMMAND_TIMEOUT);

        talon.configMotionAcceleration(500, RobotMap.TALON_COMMAND_TIMEOUT);
        talon.configMotionCruiseVelocity(800, RobotMap.TALON_COMMAND_TIMEOUT);
    }

}

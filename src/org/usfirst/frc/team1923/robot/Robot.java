package org.usfirst.frc.team1923.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.autonomous.AutonManager;
import org.usfirst.frc.team1923.robot.commands.auton.*;
import org.usfirst.frc.team1923.robot.commands.auton.center.CenterLSwitchAuton;
import org.usfirst.frc.team1923.robot.commands.auton.center.CenterRSwitchAuton;
import org.usfirst.frc.team1923.robot.commands.auton.left.*;
import org.usfirst.frc.team1923.robot.commands.auton.right.RightRScaleAuton;
import org.usfirst.frc.team1923.robot.commands.auton.right.RightRSwitchAuton;
import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team1923.robot.utils.PIDF;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class Robot extends TimedRobot {

    public static DrivetrainSubsystem drivetrainSubsystem;
    public static ElevatorSubsystem elevatorSubsystem;
    public static IntakeSubsystem intakeSubsystem;

    public static OI oi;

    public static AutonManager autonManager;
    private Command autonCommand;

    public static double time;

    @Override
    public void robotInit() {
        TrajectoryStore.loadTrajectories();

        drivetrainSubsystem = new DrivetrainSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystem = new IntakeSubsystem();

        oi = new OI();

        autonManager = new AutonManager();
        autonManager.add(new LeftLSwitchAuton())
                .add(new LeftLScaleAuton())
                .add(new LeftLScaleBAuton())
                .add(new LeftRScaleAuton())
                .add(new LeftParkCenterAuton())
                .add(new CenterLSwitchAuton())
                .add(new CenterRSwitchAuton())
                .add(new RightRSwitchAuton())
                .add(new RightRScaleAuton())
                .add(new CrossLineAuton())
                .add(new DoNothingAuton());
        autonManager.start();

        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(180, 100);
        camera.setFPS(15);
    }

    @Override
    public void autonomousInit() {
        this.autonCommand = autonManager.getSelectedAuton();

        if (this.autonCommand != null) {
            this.autonCommand.start();
        }

        time = Timer.getFPGATimestamp();
    }

    @Override
    public void autonomousPeriodic() {
        // In case the FMS does not send data at autonomousInit()
        if (this.autonCommand == null) {
            this.autonomousInit();
        }
    }

    @Override
    public void teleopInit() {
        if (this.autonCommand != null) {
            this.autonCommand.cancel();
        }

        Robot.drivetrainSubsystem.getLeftMaster().clearStickyFaults(15);
        Robot.drivetrainSubsystem.getLeftMaster().setIntegralAccumulator(0, PIDF.PRIMARY_LOOP, 15);
        Robot.drivetrainSubsystem.getLeftMaster().setIntegralAccumulator(0, PIDF.AUXILIARY_LOOP, 15);

        Robot.drivetrainSubsystem.getRightMaster().clearStickyFaults(15);
        Robot.drivetrainSubsystem.getRightMaster().setIntegralAccumulator(0, PIDF.PRIMARY_LOOP, 15);
        Robot.drivetrainSubsystem.getRightMaster().setIntegralAccumulator(0, PIDF.AUXILIARY_LOOP, 15);
    }

    @Override
    public void disabledInit() {
        if (this.autonCommand != null) {
            this.autonCommand.cancel();
        }

        this.autonCommand = null;
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();

        SmartDashboard.putString("Match Time Remaining", Math.round(DriverStation.getInstance().getMatchTime()) + "");
        SmartDashboard.putNumber("Sensor Sum", Robot.drivetrainSubsystem.getLeftMaster().getSelectedSensorPosition(PIDF.PRIMARY_LOOP));
    }

}

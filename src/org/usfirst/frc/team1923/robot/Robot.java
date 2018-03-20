package org.usfirst.frc.team1923.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
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
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class Robot extends TimedRobot {

    public static DrivetrainSubsystem drivetrainSubsystem;
    public static ElevatorSubsystem elevatorSubsystem;
    public static IntakeSubsystem intakeSubsystem;
    public static LEDSubsystem ledSubsystem;

    public static OI oi;

    public static AutonManager autonManager;
    private Command autonCommand;

    @Override
    public void robotInit() {
        TrajectoryStore.loadTrajectories();

        drivetrainSubsystem = new DrivetrainSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        ledSubsystem = new LEDSubsystem();

        oi = new OI();

        autonManager = new AutonManager();
        autonManager.add(new LeftLSwitchAuton())
                .add(new LeftLScaleAuton())
                .add(new LeftLScaleLSwitchAuton())
                .add(new LeftRScaleAuton())
                .add(new CenterLSwitchAuton())
                .add(new CenterRSwitchAuton())
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
        SmartDashboard.putNumber("Heading", Robot.drivetrainSubsystem.getHeading());
    }

}

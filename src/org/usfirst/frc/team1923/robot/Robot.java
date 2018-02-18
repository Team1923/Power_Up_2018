package org.usfirst.frc.team1923.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import org.usfirst.frc.team1923.robot.autonomous.AutonManager;
import org.usfirst.frc.team1923.robot.commands.auton.*;
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
        System.out.println("Loading trajectories...");
        TrajectoryStore.loadTrajectories();
        System.out.println("Loaded all trajectories.");

        drivetrainSubsystem = new DrivetrainSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        ledSubsystem = new LEDSubsystem();

        oi = new OI();

        autonManager = new AutonManager();
        autonManager.add(new CenterLScaleAuton())
                .add(new CenterLSwitchAuton())
                .add(new CenterRScaleAuton())
                .add(new CenterRSwitchAuton())
                .add(new CrossLineLongAuton())
                .add(new CrossLineShortAuton())
                .add(new DoNothingAuton())
                .add(new LeftLScaleAuton())
                .add(new LeftLSwitchAuton())
                .add(new RightRScaleAuton())
                .add(new RightRSwitchAuton());
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
    public void robotPeriodic() {
        Scheduler.getInstance().run();

        autonManager.periodic();
    }

}

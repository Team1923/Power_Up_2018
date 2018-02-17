package org.usfirst.frc.team1923.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.autonomous.AutonManager;
import org.usfirst.frc.team1923.robot.autonomous.Autonomous;
import org.usfirst.frc.team1923.robot.autonomous.SendablePriorityList;
import org.usfirst.frc.team1923.robot.commands.auton.CenterLScaleAuton;
import org.usfirst.frc.team1923.robot.commands.auton.CenterLSwitchAuton;
import org.usfirst.frc.team1923.robot.commands.auton.CenterRScaleAuton;
import org.usfirst.frc.team1923.robot.commands.auton.CenterRSwitchAuton;
import org.usfirst.frc.team1923.robot.commands.auton.CrossLineLongAuton;
import org.usfirst.frc.team1923.robot.commands.auton.CrossLineShortAuton;
import org.usfirst.frc.team1923.robot.commands.auton.DoNothingAuton;
import org.usfirst.frc.team1923.robot.commands.auton.LeftLScaleAuton;
import org.usfirst.frc.team1923.robot.commands.auton.LeftLSwitchAuton;
import org.usfirst.frc.team1923.robot.commands.auton.RightRScaleAuton;
import org.usfirst.frc.team1923.robot.commands.auton.RightRSwitchAuton;
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
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void teleopInit() {
        // cancel auton command
    }

}

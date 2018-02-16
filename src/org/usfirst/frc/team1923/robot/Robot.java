package org.usfirst.frc.team1923.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    private Command autonomousCommand;
    private SendablePriorityList priorityList;

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

        this.priorityList = new SendablePriorityList();
        this.priorityList.add(new CenterLScaleAuton(), new CenterLSwitchAuton(), new CenterRScaleAuton(), new CenterRSwitchAuton(), new CrossLineLongAuton(), new CrossLineShortAuton(), new DoNothingAuton(), new LeftLScaleAuton(), new LeftLSwitchAuton(), new RightRScaleAuton(), new RightRSwitchAuton());
        SmartDashboard.putData("Autonomous Mode Priority List", priorityList);
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        for (Command command : this.priorityList.getOrder()) {
            if (true) { // TODO check if the command can be run under the current field configuration
                this.autonomousCommand = command;
                command.start();
                break;
            }
        }
    }

    @Override
    public void teleopInit() {
        if (this.autonomousCommand != null) {
            this.autonomousCommand.cancel();
        }
    }

}

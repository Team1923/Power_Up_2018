package org.usfirst.frc.team1923.robot;

import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

    public static OI oi;
    public static DrivetrainSubsystem driveSubSys;

    private Command autonomousCommand;
    private SendableChooser<Command> chooser = new SendableChooser<>();

    @Override
    public void robotInit() {
        oi = new OI();

        SmartDashboard.putData("Autonomous Mode", chooser);
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        this.autonomousCommand = chooser.getSelected();

        if (this.autonomousCommand != null) {
            this.autonomousCommand.start();
        }
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        if (this.autonomousCommand != null) {
            this.autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }

}

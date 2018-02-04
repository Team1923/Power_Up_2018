package org.usfirst.frc.team1923.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team1923.robot.utils.battery.BatteryMonitor;

public class Robot extends IterativeRobot {

    public static OI oi;

    public static ElevatorSubsystem elevatorSubsystem;
    public static DrivetrainSubsystem drivetrainSubsystem;
    public static LEDSubsystem ledSubsystem;
    public static IntakeSubsystem intakeSubsystem;

    public static BatteryMonitor batteryMonitor;

    private Command autonomousCommand;
    private SendableChooser<Command> chooser = new SendableChooser<>();

    @Override
    public void robotInit() {
        oi = new OI();

        drivetrainSubsystem = new DrivetrainSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        ledSubsystem = new LEDSubsystem();

        batteryMonitor = new BatteryMonitor();

        SmartDashboard.putData("Autonomous Mode", this.chooser);
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();

        batteryMonitor.tick();
    }

    @Override
    public void autonomousInit() {
        this.autonomousCommand = this.chooser.getSelected();

        if (this.autonomousCommand != null) {
            this.autonomousCommand.start();
        }
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

        batteryMonitor.tick();
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

        intakeSubsystem.refreshSensors();
        batteryMonitor.tick();
    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }

}

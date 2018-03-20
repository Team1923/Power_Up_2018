package org.usfirst.frc.team1923.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1923.robot.autonomous.AutonManager;
import org.usfirst.frc.team1923.robot.commands.auton.*;
import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;
import org.usfirst.frc.team1923.robot.utils.logger.Logger;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;

public class Robot extends TimedRobot {

    public static DrivetrainSubsystem drivetrainSubsystem;
    public static ElevatorSubsystem elevatorSubsystem;
    public static IntakeSubsystem intakeSubsystem;
    public static LEDSubsystem ledSubsystem;

    public static OI oi;
 //   public static Logger logger;

    public static AutonManager autonManager;
    private Command autonCommand;

    private PowerDistributionPanel pdp;

    @Override
    public void robotInit() {
        System.out.println("Loading trajectories...");
        TrajectoryStore.loadTrajectories();
        System.out.println("Loaded all trajectories.");

        this.pdp = new PowerDistributionPanel(RobotMap.Robot.PDP_PORT);

        drivetrainSubsystem = new DrivetrainSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        ledSubsystem = new LEDSubsystem();

        oi = new OI();

//        logger = new Logger();
//
//        logger.addDataSource("DriverStation_Alliance", () -> DriverStation.getInstance().getAlliance().name());
//        logger.addDataSource("DriverStation_GameSpecificMessage", () -> DriverStation.getInstance().getGameSpecificMessage());
//        logger.addDataSource("DriverStation_MatchTime", () -> String.format("%.4g%n", DriverStation.getInstance().getMatchTime()));
//
//        logger.addDataSource("PDP_Voltage", () -> String.format("%.4g%n", this.pdp.getVoltage()));
//        logger.addDataSource("PDP_Current", () -> String.format("%.4g%n", this.pdp.getTotalCurrent()));
//        logger.addDataSource("PDP_Temp", () -> String.format("%.4g%n", this.pdp.getTemperature()));
//
//        for (int i = 0; i < 16; i++) {
//            logger.addDataSource("PDP_Current" + i, () -> String.format("%.4g%n", this.pdp.getCurrent(0)));
//        }

        autonManager = new AutonManager();
        autonManager.add(new CenterLSwitchAuton())
                .add(new CenterRSwitchAuton())
                .add(new SCrossLineShortAuton())
                .add(new DoNothingAuton())
                .add(new SLeftLSwitchAuton())
                .add(new SRightRSwitchAuton());
    }

    @Override
    public void autonomousInit() {
        this.autonCommand = autonManager.getSelectedAuton();

        if (this.autonCommand != null) {
            this.autonCommand.start();
        }

//        if (!logger.isActive()) {
//            logger.initialize();
//        }
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

//        if (!logger.isActive()) {
//            logger.initialize();
//        }
    }

    @Override
    public void disabledInit() {
        if (this.autonCommand != null) {
            this.autonCommand.cancel();
        }

        this.autonCommand = null;

   //     logger.save();
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();

        autonManager.periodic();

        SmartDashboard.putNumber("Match Time Remaining", DriverStation.getInstance().getMatchTime());
      //  logger.periodic();
    }

}

package org.usfirst.frc.team1923.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.util.Arrays;

import org.usfirst.frc.team1923.robot.subsystems.DrivetrainSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.ElevatorSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team1923.robot.subsystems.LEDSubsystem;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore;
import org.usfirst.frc.team1923.robot.utils.pathfinder.TrajectoryStore.Waypoints;

public class Robot extends TimedRobot {

    public static DrivetrainSubsystem drivetrainSubsystem;
    public static ElevatorSubsystem elevatorSubsystem;
    public static IntakeSubsystem intakeSubsystem;
    public static LEDSubsystem ledSubsystem;

    public static OI oi;

    private Command autonomousCommand;
    private SendableChooser<Command> chooser = new SendableChooser<>();

    @Override
    public void robotInit() {
//        System.out.println("Loading trajectories...");
//
//        TrajectoryStore.loadTrajectories();
//        
//        System.out.println(Arrays.toString(Waypoints.STRAIGHT_2M.getWaypoints()));
//        
//        Trajectory trajectory = TrajectoryStore.loadTrajectory(Waypoints.STRAIGHT_2M);
//        
//        for (Segment segment : trajectory.segments) {
//        	System.out.print("(" + r(segment.x) + ", " + r(segment.y) + "), ");
//        }
//        
//        TankModifier mod = new TankModifier(trajectory).modify(0.75);
//        
//        System.out.println("---------");
//        
//        for (Segment segment : mod.getLeftTrajectory().segments) {
//        	System.out.print("(" + r(segment.x) + ", " + r(segment.y) + "), ");
//        }
//        
//        for (Segment segment : mod.getRightTrajectory().segments) {
//        	System.out.print("(" + r(segment.x) + ", " + r(segment.y) + "), ");
//        }
//
//        System.out.println("Loaded all trajectories.");

        drivetrainSubsystem = new DrivetrainSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        intakeSubsystem = new IntakeSubsystem();
        ledSubsystem = new LEDSubsystem();

        oi = new OI();

        SmartDashboard.putData("Autonomous Mode", this.chooser);
    }
    
    public static double r(double r) {
    	return Math.round(r * 1000) / 1000.0;
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
        
        SmartDashboard.putNumber("Left Encoder", Robot.drivetrainSubsystem.getLeftEncoderPosition());
        SmartDashboard.putNumber("Right Encoder", Robot.drivetrainSubsystem.getRightEncoderPosition());
        
        SmartDashboard.putNumber("RT", Robot.oi.operator.rightTrigger.getX());
        SmartDashboard.putNumber("LT", Robot.oi.operator.leftTrigger.getX());
        SmartDashboard.putNumber("RE", Robot.drivetrainSubsystem.getRightPosition());
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
        
        SmartDashboard.putNumber("Left Encoder", Robot.drivetrainSubsystem.getLeftEncoderPosition());
        SmartDashboard.putNumber("Right Encoder", Robot.drivetrainSubsystem.getRightEncoderPosition());
    }

    @Override
    public void testPeriodic() {
    }

    public static double i2m(double inches) {
        return 0.0254 * inches;
    }

}

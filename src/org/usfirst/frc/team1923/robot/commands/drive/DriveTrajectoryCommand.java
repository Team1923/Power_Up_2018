package org.usfirst.frc.team1923.robot.commands.drive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderJNI;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrajectoryCommand extends Command {
      
    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
    	
    }

    @Override
    protected void end() {
        Robot.drivetrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
    
    public DriveTrajectoryCommand() {
    	getTrajectory();
    }
    
    public Trajectory getTrajectory() {
    	return trajectory;
    }

}

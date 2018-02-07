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
	public FitMethod fit = Trajectory.FitMethod.HERMITE_CUBIC;
	public int samples = Trajectory.Config.SAMPLES_HIGH;
	public double timeStep = 0.05;
	static boolean libLoaded;
	Trajectory trajectory;
	Waypoint[] points;
	public double maxVelocity = 1.7; // in m/s
    public double maxAcceleration = 2.0; // in m/s/s
    public double maxJerk = 60.0; // in m/s/s/s
    Trajectory.Config config;
    public Waypoint[] DRIVE_STRAIGHT_5M = new Waypoint[] {
            new Waypoint(0, 1, 2),
            new Waypoint(0, 1, 2),
            new Waypoint(0, 1, 2),
    };
    public Waypoint[] LEFT_SWITCH_AUTOM = new Waypoint[] {
  		  new Waypoint(0, 1, 2),
  		  new Waypoint(0, 1, 2),
  		  new Waypoint(0, 1, 2),
    };
    
	static {
		if (libLoaded == false) {
			try {
				System.loadLibrary("pathfinderjava");
				libLoaded = true;
			} catch (Exception e) {
				System.out.println("ERROR LOADING PATHFINDER!\nPATHFINDER IS NOT GOING TO WORK!");
			}
		}
		
	}
	
    public DriveTrajectoryCommand(Waypoint[] points) {
    	
    	this.points = points;
        
        config = new Trajectory.Config(fit, samples, timeStep, maxVelocity, maxAcceleration, maxJerk); 
        trajectory = Pathfinder.generate(points, config);
        
        for (int i = 0; i < trajectory.length(); i++) {
    	    Trajectory.Segment seg = trajectory.get(i);
    	    
    	    System.out.printf("%f,%f,%f,%f,%f,%f,%f,%f\n", 
    	        seg.dt, seg.x, seg.y, seg.position, seg.velocity, 
    	            seg.acceleration, seg.jerk, seg.heading);
    	}
        
        //TODO set waypoints
        
     // The distance between the left and right sides of the wheelbase is 0.6m
    	double wheelbase_width = 0.6;

    	// Create the Modifier Object
    	TankModifier modifier = new TankModifier(trajectory);

    	// Generate the Left and Right trajectories using the original trajectory
    	// as the centre
    	modifier.modify(wheelbase_width);

    	Trajectory left  = modifier.getLeftTrajectory();       // Get the Left Side
    	Trajectory right = modifier.getRightTrajectory();      // Get the Right Side    	
		
    	
    }
    
    @Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
    
    public Trajectory getTrajectory() {
    	return trajectory;
    }
    
    public void genTrajectory(Waypoint[] waypoints) {
    	Pathfinder.generate(waypoints, config);
    }
  
}

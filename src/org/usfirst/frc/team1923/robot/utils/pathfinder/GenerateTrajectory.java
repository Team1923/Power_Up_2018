package org.usfirst.frc.team1923.robot.utils;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.modifiers.TankModifier;

public class GenerateTrajectory {
	// Trajectories loaded into these fields
	private Trajectory trajectory, left, right; 
	
	// Fit Method:  HERMITE_CUBIC or HERMITE_QUINTIC
	private FitMethod fit = Trajectory.FitMethod.HERMITE_CUBIC;
	
	private int samples = Trajectory.Config.SAMPLES_HIGH;
	
    private double timeStep = 0.05;
    
    // Hardcoded values that are used for generating trajectories
    public Waypoint[] DRIVE_STRAIGHT_5M = new Waypoint[] {new Waypoint(0, 0, 0), new Waypoint(5, 0, 0)}, 
    				  LEFT_SWITCH_AUTON = new Waypoint[] {new Waypoint(0, 0, 0), new Waypoint(5, -14, 90)},
    				  RIGHT_SWITCH_AUTON = new Waypoint[] {new Waypoint(0, 0, 0), new Waypoint(5, 14, -90)};
    
    public GenerateTrajectory(Waypoint[] points) {
    	this.points = points;
       	double maxVelocity = 1.7, 
       		   maxAcceleration = 2.0, 
       		   maxJerk = 60.0;
        double wheelbase_width = 0.6096;
        
           trajectory = Pathfinder.generate(points, new Trajectory.Config(fit, samples, timeStep, maxVelocity, maxAcceleration, maxJerk));
           
           for (int i = 0; i < trajectory.length(); i++) {
            Trajectory.Segment seg = trajectory.get(i);
            
            System.out.printf("%f,%f,%f,%f,%f,%f,%f,%f\n", 
                seg.dt, seg.x, seg.y, seg.position, seg.velocity, 
                    seg.acceleration, seg.jerk, seg.heading);
        }
                   
        TankModifier modifier = new TankModifier(trajectory);
        modifier.modify(wheelbase_width);
        left = modifier.getLeftTrajectory();       
        right = modifier.getRightTrajectory();    
    }
    
    public static final int NORMAL = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public Trajectory getTrajectory(int type) {
    	switch(type) {
    	case 0:
    		return trajectory;
    	case 1:
    		return left;
    	case 2:
    		return right;
    	}
    	default:
    		System.out.println("ERROR: Trajectory option not found;\nPlease specify Trajectory.{NORMAL, LEFT, or RIGHT}"));
    		break;
    }
}

package org.usfirst.frc.team1923.robot.commands.drive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderJNI;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

public class DriveTrajectoryCommand {
	
    static boolean libLoaded = false;

    static {
        if (!libLoaded) {
            try {
                System.loadLibrary("pathfinderjava");
            } catch (Exception e) {
                e.printStackTrace();
            }
            libLoaded = true;
        }
    }
    
	/**
	 * x         The X position of the waypoint in meters
     * y         The Y position of the waypoint in meters
     * angle     The exit angle of the waypoint in degrees
	 */
    // TODO set waypoints
	double x1, y1;
	double x2, y2;
	double x3, y3;
	double angle1, angle2, angle3;
	
	Waypoint[] points = new Waypoint[] {
			new Waypoint(x1, y1, Pathfinder.d2r(angle1)),
			new Waypoint(x2, y3, Pathfinder.d2r(angle2)),
			new Waypoint(x3, y3, Pathfinder.d2r(angle2)),
	};

	// Create the Trajectory Configuration
	// Fit Method:  HERMITE_CUBIC or HERMITE_QUINTIC
    public FitMethod fit = Trajectory.FitMethod.HERMITE_CUBIC;
    // Sample Count:        SAMPLES_HIGH (100 000)
    //  					SAMPLES_LOW  (10 000)
    //  					SAMPLES_FAST (1 000)
    public int samples = Trajectory.Config.SAMPLES_HIGH;
    public double timeStep = 0.05; 
    public double max_velocity = 1.7; // in m/s
    public double max_acceleration = 2.0; // in m/s/s
    public double max_jerk = 60.0; // in m/s/s/s
	Trajectory.Config config = new Trajectory.Config(fit, samples, timeStep, max_velocity, max_acceleration, max_jerk); 
	
	Trajectory trajectory = Pathfinder.generate(points, config);{
	
	for (int i = 0; i < trajectory.length(); i++) {
	    Trajectory.Segment seg = trajectory.get(i);
	    
	    System.out.printf("%f,%f,%f,%f,%f,%f,%f,%f\n", 
	        seg.dt, seg.x, seg.y, seg.position, seg.velocity, 
	            seg.acceleration, seg.jerk, seg.heading);
	}
	
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
}
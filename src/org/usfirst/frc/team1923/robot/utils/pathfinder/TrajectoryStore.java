package org.usfirst.frc.team1923.robot.utils.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.usfirst.frc.team1923.robot.RobotMap;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrajectoryStore {

    public static Trajectory.Config trajectoryConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC,
            Trajectory.Config.SAMPLES_HIGH,
            0.05,
            2.00,
            2.00,
            60.00
    );

    public static Map<String, Trajectory> trajectories = new ConcurrentHashMap<>();

    public static Trajectory loadTrajectory(Waypoints waypoints) {
        if (trajectories.containsKey(waypoints.name())) {
            return trajectories.get(waypoints.name());
        }

        File trajectoryDir = new File(RobotMap.TRAJECTORY_STORE_DIR);

        if (!trajectoryDir.exists()) {
            trajectoryDir.mkdir();
        }

        if (!trajectoryDir.isDirectory()) {
            throw new RuntimeException("Trajectory directory is not a directory.");
        }

        File trajectoryFile = new File(RobotMap.TRAJECTORY_STORE_DIR + "/" + getTrajectoryFile(waypoints));

        if (!trajectoryFile.isFile() && trajectoryFile.exists()) {
            throw new RuntimeException("Trajectory file is not a file.");
        }

        Trajectory trajectory;

        if (!trajectoryFile.exists()) {
            trajectory = Pathfinder.generate(waypoints.getWaypoints(), trajectoryConfig);

            // Pathfinder.writeToFile(trajectoryFile, trajectory);
        } else {
            trajectory = Pathfinder.readFromFile(trajectoryFile);
        }

        trajectories.put(waypoints.name(), trajectory);

        return trajectory;
    }

    public static void loadTrajectories() {
        for (Waypoints waypoints : Waypoints.values()) {
        	if (waypoints.getWaypoints().length < 2) {
        		continue;
        	}
        	
            loadTrajectory(waypoints);
        }
    }

    private static String getTrajectoryFile(Waypoints waypoints) {
        StringBuilder file = new StringBuilder();

        file.append(waypoints.name());

        for (Waypoint waypoint : waypoints.getWaypoints()) {
            file.append("[").append(waypoint.x).append(", ").append(waypoint.y).append(", ").append(waypoint.angle).append("]");
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(file.toString().getBytes());

            return waypoints.name() + "-" + new String(messageDigest.digest()).substring(0, 8) + ".traj";
        } catch (Exception e) {
            e.printStackTrace();

            return "null.traj";
        }
    }

    final double length = 32.5;
    final double width = 27.5;
    final double bumpers = 6.25;

    public static enum Waypoints {

        STRAIGHT_2M(new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(0, 3, 0)
        }),

//        LEFT_LSWITCH(new Waypoint[]{
//            new Waypoint(0, 0, 0),
//            new Waypoint(3.404826, 0, 90),
//        }),
//
//        LEFT_LSCALE(new Waypoint[] {
//            new Waypoint(0, 0, 0),
//            new Waypoint(7.4041, 0, 90)
//        }),

        CENTER_LSWITCH(new Waypoint[] {
            new Waypoint(0, 0, 0),
        }),

        CENTER_RSWITCH(new Waypoint[] {
            new Waypoint(0, 0, 0),
        }),

        RIGHT_RSWITCH(new Waypoint[] {
            new Waypoint(0, 0, 0),

        }),

        RIGHT_RSCALE(new Waypoint[] {
            new Waypoint(0, 0, 0),

        });

        private final Waypoint[] waypoints;

        private Waypoints(Waypoint[] waypoints) {
            this.waypoints = waypoints;
        }

        public Waypoint[] getWaypoints() {
            return this.waypoints;
        }

    }

}

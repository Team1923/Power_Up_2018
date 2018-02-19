package org.usfirst.frc.team1923.robot.utils.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrajectoryStore {

    public static Trajectory.Config trajectoryConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC,
            Trajectory.Config.SAMPLES_LOW,
            0.02,
            Converter.inchesToMeters(RobotMap.Drivetrain.TRAJECTORY_MAX_VELOCITY),
            Converter.inchesToMeters(RobotMap.Drivetrain.TRAJECTORY_MAX_ACCELERATION),
            Converter.inchesToMeters(RobotMap.Drivetrain.TRAJECTORY_MAX_JERK)
    );

    public static Map<String, Trajectory> trajectories = new ConcurrentHashMap<>();

    public static Trajectory loadTrajectory(Waypoints waypoints) {
        if (trajectories.containsKey(waypoints.name())) {
            return trajectories.get(waypoints.name());
        }

        File trajectoryDir = new File(RobotMap.Drivetrain.TRAJECTORY_STORE_DIR);

        if (!trajectoryDir.exists()) {
            trajectoryDir.mkdir();
        }

        if (!trajectoryDir.isDirectory()) {
            throw new RuntimeException("Trajectory directory is not a directory.");
        }

        File trajectoryFile = new File(RobotMap.Drivetrain.TRAJECTORY_STORE_DIR + "/" + getTrajectoryFile(waypoints));

        if (!trajectoryFile.isFile() && trajectoryFile.exists()) {
            throw new RuntimeException("Trajectory file is not a file.");
        }

        Trajectory trajectory;

        if (!trajectoryFile.exists()) {
            trajectory = Pathfinder.generate(waypoints.getWaypoints(), trajectoryConfig);

            Pathfinder.writeToFile(trajectoryFile, trajectory);
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

    public enum Waypoints {

        LEFT_LSCALE(new Waypoint[] {
                new Waypoint(0, Converter.inchesToMeters(104.5), 0),
                new Waypoint(Converter.inchesToMeters(168), Converter.inchesToMeters(120), 0),
                new Waypoint(Converter.inchesToMeters(268), Converter.inchesToMeters(100), Pathfinder.d2r(135)),
        }),

        CENTER_LSWITCH(new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(Converter.inchesToMeters(95), Converter.inchesToMeters(50), Pathfinder.d2r(0)),
        }),

        CENTER_RSWITCH(new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(Converter.inchesToMeters(95), -1 * Converter.inchesToMeters(50), Pathfinder.d2r(0)),
        }),

        CENTER_LSCALE(new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(Converter.inchesToMeters(148), Converter.inchesToMeters(130), 0),
                new Waypoint(Converter.inchesToMeters(268), Converter.inchesToMeters(100), Pathfinder.d2r(-45)),
        }),

        CENTER_RSCALE(new Waypoint[] {
                new Waypoint(0, 0, 0),
                new Waypoint(Converter.inchesToMeters(148), -1 * Converter.inchesToMeters(130), 0),
                new Waypoint(Converter.inchesToMeters(268), -1 * Converter.inchesToMeters(100), Pathfinder.d2r(45)),
        }),

        RIGHT_RSWITCH(new Waypoint[] {
                new Waypoint(0, -1 * Converter.inchesToMeters(104.5), 0),
                new Waypoint(Converter.inchesToMeters(100), -1 * Converter.inchesToMeters(100), Pathfinder.d2r(-135)),
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

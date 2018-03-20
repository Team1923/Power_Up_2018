package org.usfirst.frc.team1923.robot.utils.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;

public class TrajectoryStore {

    public static Trajectory.Config trajectoryConfig = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_QUINTIC,
            Trajectory.Config.SAMPLES_LOW,
            0.02,
            Converter.inchesToFeet(RobotMap.Drivetrain.TRAJ_MAX_VELOCITY),
            Converter.inchesToFeet(RobotMap.Drivetrain.TRAJ_MAX_ACCELERATION),
            Converter.inchesToFeet(RobotMap.Drivetrain.TRAJ_MAX_JERK)
    );

    public static Map<String, Trajectory> trajectories = new ConcurrentHashMap<>();

    private static final Waypoint LEFT_STARTING_POSITION = new Waypoint(2.157953, 24.155903, Pathfinder.d2r(-39.805571));
    private static final Waypoint CENTER_STARTING_POSITION = new Waypoint(1.64583, 13.0625, 0);
    private static final Waypoint RIGHT_STARTING_POSITION = new Waypoint(2.157953, 2.844097, Pathfinder.d2r(39.805571));

    private static final Waypoint SRIGHT_STARTING_POSITION = new Waypoint(1.64583, 3.9375, 0);

    private static final Waypoint LEFT_SCALE_POSITION = new Waypoint(22.559610, 22.708780, Pathfinder.d2r(-35));
    private static final Waypoint RIGHT_SCALE_POSITION = new Waypoint(22.559610, 4.291220, Pathfinder.d2r(35));

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

            System.out.println("Loading trajectory " + waypoints.name());
            
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

        CENTER_RSWITCHLAYUP(new Waypoint[]{
                CENTER_STARTING_POSITION,
                new Waypoint(10, 8.5, 0)
        }),

        CENTER_LSWITCHLAYUP(new Waypoint[] {
                CENTER_STARTING_POSITION,
                new Waypoint(10, 20, 0)
        }),

        CENTER_RSCALE(new Waypoint[] {
                CENTER_STARTING_POSITION,
                new Waypoint(13, 3, 0),
                RIGHT_SCALE_POSITION
        }),

        CENTER_LSCALE(new Waypoint[] {
                CENTER_STARTING_POSITION,
                new Waypoint(14, 3, 0),
                LEFT_SCALE_POSITION
        }),

        RIGHT_RSWITCHLAYUP(new Waypoint[] {
                RIGHT_STARTING_POSITION,
                new Waypoint(10.536220, 5.941220, Pathfinder.d2r(45))
        }),

        RIGHT_RSCALE(new Waypoint[] {
                RIGHT_STARTING_POSITION,
                RIGHT_SCALE_POSITION
        }),

        RIGHT_LSCALE(new Waypoint[] {
                RIGHT_STARTING_POSITION,
                new Waypoint(10, 3, 0),
                new Waypoint(19.50, 20, 90),
                new Waypoint(22.122345, 21.771065, Pathfinder.d2r(-15))
        }),

        LEFT_LSWITCHLAYUP(new Waypoint[] {
                LEFT_STARTING_POSITION,
                new Waypoint(10.536220, 21.058780, Pathfinder.d2r(-45))
        }),

        LEFT_RSCALE(new Waypoint[] {
                LEFT_STARTING_POSITION,
                new Waypoint(10, 24, 0),
                new Waypoint(19.5, 7, Pathfinder.d2r(-90)),
                new Waypoint(22.122346, 5.228935, Pathfinder.d2r(15))
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

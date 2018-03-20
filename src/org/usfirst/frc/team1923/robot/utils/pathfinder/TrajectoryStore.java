package org.usfirst.frc.team1923.robot.utils.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;

public class TrajectoryStore {

    public static Map<String, Trajectory> trajectories = new ConcurrentHashMap<>();

    private static final Waypoint LEFT_STARTING_POSITION = new Waypoint(1.65, 23, 0);
    private static final Waypoint CENTER_STARTING_POSITION = new Waypoint(1.65, 13, 0);
    private static final Waypoint RIGHT_STARTING_POSITION = new Waypoint(1.65, 3.94, 0);

    private static final Waypoint LEFT_SCALE_POSITION = new Waypoint(23, 19.7, Pathfinder.d2r(-35));
    private static final Waypoint RIGHT_SCALE_POSITION = new Waypoint(22.5, 9.5, Pathfinder.d2r(35));

    public static Trajectory loadTrajectory(Path path) {
        if (trajectories.containsKey(path.name())) {
            return trajectories.get(path.name());
        }

        File trajectoryDir = new File(RobotMap.Drivetrain.TRAJECTORY_STORE_DIR);

        if (!trajectoryDir.exists()) {
            trajectoryDir.mkdir();
        }

        if (!trajectoryDir.isDirectory()) {
            throw new RuntimeException("Trajectory directory is not a directory.");
        }

        File trajectoryFile = new File(RobotMap.Drivetrain.TRAJECTORY_STORE_DIR + "/" + path.name() + "-" + path.getUniqueId() + ".traj");

        if (!trajectoryFile.isFile() && trajectoryFile.exists()) {
            throw new RuntimeException("Trajectory file is not a file.");
        }

        Trajectory trajectory;

        if (!trajectoryFile.exists()) {
            trajectory = Pathfinder.generate(path.getWaypoints(), path.getTrajectoryConfig());

            Pathfinder.writeToFile(trajectoryFile, trajectory);
        } else {
            trajectory = Pathfinder.readFromFile(trajectoryFile);
        }

        trajectories.put(path.name(), trajectory);

        return trajectory;
    }

    public static void loadTrajectories() {
        for (Path path : Path.values()) {
            if (path.getWaypoints().length < 2) {
                continue;
            }

            System.out.println("Loading trajectory " + path.name());

            loadTrajectory(path);
        }
    }

    public enum Path {

        CENTER_RSWITCHLAYUP(new Waypoint[]{
                CENTER_STARTING_POSITION,
                new Waypoint(10, 8.5, 0)
        }),

        CENTER_LSWITCHLAYUP(new Waypoint[] {
                CENTER_STARTING_POSITION,
                new Waypoint(10, 19, 0)
        }),

        RIGHT_PARKCENTER(new Waypoint[] {
                RIGHT_STARTING_POSITION,
                new Waypoint(19.5, 13.5, Pathfinder.d2r(90))
        }, 60.0),

        LEFT_PARKCENTER(new Waypoint[] {
                LEFT_STARTING_POSITION,
                new Waypoint(19, 14, Pathfinder.d2r(-90))
        }, 40.0),

//        RIGHT_RSWITCHLAYUP(new Waypoint[] {
//                RIGHT_STARTING_POSITION,
//                new Waypoint(10.536220, 5.941220, Pathfinder.d2r(45))
//        }),
//
        RIGHT_RSCALE(new Waypoint[] {
                RIGHT_STARTING_POSITION,
                RIGHT_SCALE_POSITION
        }),

        LEFT_LSCALE(new Waypoint[]{
                LEFT_STARTING_POSITION,
                LEFT_SCALE_POSITION
        });
//
//        RIGHT_LSCALE(new Waypoint[] {
//                RIGHT_STARTING_POSITION,
//                new Waypoint(10, 3, 0),
//                new Waypoint(19.50, 20, 90),
//                new Waypoint(22.122345, 21.771065, Pathfinder.d2r(-15))
//        }),
//
//        LEFT_LSWITCHLAYUP(new Waypoint[] {
//                LEFT_STARTING_POSITION,
//                new Waypoint(10.536220, 21.058780, Pathfinder.d2r(-45))
//        }),
//
//        LEFT_RSCALE(new Waypoint[] {
//                LEFT_STARTING_POSITION,
//                new Waypoint(10, 24, 0),
//                new Waypoint(19.5, 7, Pathfinder.d2r(-90)),
//                new Waypoint(22.122346, 5.228935, Pathfinder.d2r(15))
//        });

        private final Waypoint[] waypoints;

        private final double velocity;
        private final double acceleration;
        private final double jerk;

        private Path(Waypoint[] waypoints) {
            this(
                    waypoints,
                    RobotMap.Drivetrain.TRAJ_MAX_VELOCITY,
                    RobotMap.Drivetrain.TRAJ_MAX_ACCELERATION,
                    RobotMap.Drivetrain.TRAJ_MAX_JERK
            );
        }

        private Path(Waypoint[] waypoints, double maxVelocityAcceleration) {
            this(
                    waypoints,
                    maxVelocityAcceleration,
                    maxVelocityAcceleration,
                    RobotMap.Drivetrain.TRAJ_MAX_JERK
            );
        }

        private Path(Waypoint[] waypoints, double velocity, double acceleration, double jerk) {
            this.waypoints = waypoints;

            this.velocity = velocity;
            this.acceleration = acceleration;
            this.jerk = jerk;
        }

        public Waypoint[] getWaypoints() {
            return this.waypoints;
        }

        public int getUniqueId() {
            StringBuilder hash = new StringBuilder();

            hash.append("Path{waypoints=");

            StringBuilder waypoints = new StringBuilder();

            waypoints.append("[");

            for (Waypoint waypoint : this.waypoints) {
                waypoints.append("Waypoint{x=")
                        .append(String.format("%.4f", waypoint.x))
                        .append(", y=")
                        .append(String.format("%.4f", waypoint.y))
                        .append(", angle=")
                        .append(String.format("%.4f", waypoint.angle))
                        .append("}, ");
            }

            hash.append(waypoints.toString().replaceAll(", $", ""))
                    .append("], velocity=")
                    .append(String.format("%.4f", this.velocity))
                    .append(", acceleration=")
                    .append(String.format("%.4f", this.acceleration))
                    .append(", jerk=")
                    .append(String.format("%.4f", this.jerk))
                    .append("}");

            return Math.abs(hash.toString().hashCode());
        }

        public Trajectory.Config getTrajectoryConfig() {
            return new Trajectory.Config(
                    Trajectory.FitMethod.HERMITE_QUINTIC,
                    Trajectory.Config.SAMPLES_LOW,
                    0.02,
                    Converter.inchesToFeet(this.velocity),
                    Converter.inchesToFeet(this.acceleration),
                    Converter.inchesToFeet(this.jerk)
            );
        }

    }

}
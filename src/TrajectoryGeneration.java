import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TrajectoryGeneration {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: TrajectoryGeneration <config>");
            System.exit(0);
        }

        Waypoint[] waypoints;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));

            StringBuilder config = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                config.append(line);
            }

            JSONObject
        } catch (Exception e) {
            e.printStackTrace();

            System.exit(0);
        }

        Trajectory trajectory = Pathfinder.generate(new Waypoint[] {
                new Waypoint(0, 0, Pathfinder.d2r(0)),
                new Waypoint(10, 10, Pathfinder.d2r(90))
        }, new Trajectory.Config(
                Trajectory.FitMethod.HERMITE_QUINTIC,
                Trajectory.Config.SAMPLES_HIGH,
                0.05,
                6.00,
                2.00,
                60.00
        ));

        StringBuilder stringBuilder = new StringBuilder();

        for (Trajectory.Segment segment : trajectory.segments) {
            stringBuilder.append("(").append(round(segment.x)).append(", ").append(round(segment.y)).append("), ");
        }

        System.out.println(stringBuilder.toString().replaceAll(", $", ""));
    }

    public static double round(double d) {
        return Math.round(d * 1000) / 1000.0;
    }

}

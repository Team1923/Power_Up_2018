package org.usfirst.frc.team1923.robot.utils.pathfinder;

import com.ctre.phoenix.motion.TrajectoryPoint;
import jaci.pathfinder.Trajectory;

public class TrajectoryUtils {

    public static Trajectory invert(Trajectory trajectory) {
        trajectory = trajectory.copy();

        for (Trajectory.Segment segment : trajectory.segments) {
            segment.position = -segment.position;
            segment.velocity = -segment.velocity;
            segment.acceleration = -segment.acceleration;
            segment.jerk = -segment.jerk;
        }

        return trajectory;
    }

    public static Trajectory correctHeadings(Trajectory trajectory) {
        trajectory = trajectory.copy();

        if (trajectory.segments.length < 2) {
            return trajectory;
        }

        double initialHeading = trajectory.segments[0].heading;
        double previousHeading = initialHeading;
        double continuousHeading = initialHeading;

        for (int i = 0; i < trajectory.segments.length; i++) {
            double heading = trajectory.segments[i].heading;

            while (heading < 0) {
                heading += 2.0 * Math.PI;
            }

            while (heading >= 2.0 * Math.PI) {
                heading -= 2.0 * Math.PI;
            }

            double dH = heading - previousHeading;

            if (Math.abs(dH) > 0.5 * Math.PI) {
                if (previousHeading >= 0 && previousHeading < 0.5 * Math.PI && heading > 1.5 * Math.PI && heading < 2.0 * Math.PI) {
                    dH = -(previousHeading + 2.0 * Math.PI - heading);
                }

                if (previousHeading > 1.5 * Math.PI && previousHeading < 2.0 * Math.PI && heading >= 0 && heading < 1.5 * Math.PI) {
                    dH = 2.0 * Math.PI - previousHeading + heading;
                }
            }

            continuousHeading += dH;
            previousHeading = heading;

            trajectory.segments[i].heading = continuousHeading - initialHeading;
        }

        return trajectory;
    }

}

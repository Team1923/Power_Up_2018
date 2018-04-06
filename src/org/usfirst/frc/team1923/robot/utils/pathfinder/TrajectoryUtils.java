package org.usfirst.frc.team1923.robot.utils.pathfinder;

import com.ctre.phoenix.motion.TrajectoryPoint;
import jaci.pathfinder.Trajectory;
import org.usfirst.frc.team1923.robot.Robot;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

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

    public static Trajectory addTurn(Trajectory trajectory, double target, double velocity, double acceleration, boolean invertVelocity) {
        trajectory = trajectory.copy();

        int startingHeading = (int) (Math.toDegrees(trajectory.segments[trajectory.segments.length - 1].heading) * 10);
        acceleration /= 10;

        boolean invert = target < 0;
        target = Math.abs(target * 10);

        int steps = (int) Math.round(velocity < Math.sqrt(acceleration * target) ? target / velocity + velocity / acceleration : 2 * Math.sqrt(target / acceleration));
        double cutoff = Math.min(velocity / acceleration, steps / 2);

        Trajectory.Segment[] segments = new Trajectory.Segment[trajectory.segments.length + steps * 5];

        for (int i = 0; i < 5 * steps; ++i) {
            Trajectory.Segment segment = new Trajectory.Segment(
                    0.02,
                    trajectory.segments[trajectory.segments.length - 1].x,
                    trajectory.segments[trajectory.segments.length - 1].y,
                    trajectory.segments[trajectory.segments.length - 1].position,
                    0, 0, 0, 0
            );

            if ((i / 5.0) <= cutoff) {
                segment.velocity = acceleration * (i / 5.0);
                segment.heading = acceleration * (i / 5.0) * (i / 5.0) / 2;
            } else if ((i / 5.0) >= steps - cutoff) {
                segment.velocity = acceleration * (steps - (i / 5.0));
                segment.heading = target - acceleration * Math.pow(steps - (i / 5.0), 2) / 2;
            } else {
                segment.velocity = velocity;
                segment.heading = acceleration * cutoff * cutoff / 2 + velocity * ((i / 5.0) - cutoff);
            }

            if (invert) {
                segment.velocity *= -1;
                segment.heading *= -1;
            }

            segment.velocity *= 10;

            segment.velocity = Converter.inchesToTicks(
                    segment.velocity * Math.PI * RobotMap.Drivetrain.WHEELBASE_WIDTH / 3600 / 4.15,
                    RobotMap.Drivetrain.WHEEL_DIAMETER
            );
            segment.heading = Math.toRadians(segment.heading / 10.0 + startingHeading);

            if (invertVelocity) {
                segment.velocity *= -1.0;
            }

            segments[i + trajectory.segments.length] = segment;
        }

        trajectory.segments = segments;

        return trajectory;
    }

}

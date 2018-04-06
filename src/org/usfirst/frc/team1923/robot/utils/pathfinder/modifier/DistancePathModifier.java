package org.usfirst.frc.team1923.robot.utils.pathfinder.modifier;

import com.ctre.phoenix.motion.TrajectoryPoint;
import org.usfirst.frc.team1923.robot.RobotMap;
import org.usfirst.frc.team1923.robot.utils.Converter;
import org.usfirst.frc.team1923.robot.utils.PIDF;

public class DistancePathModifier extends PathModifier {

    private double distance;
    private double velocity;
    private double acceleration;
    private double endingVelocity;

    private boolean matchVelocity;

    public DistancePathModifier(double distance, double velocity, double acceleration) {
        this(distance, velocity, acceleration, 0);
    }

    public DistancePathModifier(double distance, double velocity, double acceleration, double endingVelocity) {
        this.distance = Converter.inchesToTicks(distance, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.velocity = Converter.inchesToTicks(velocity, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.acceleration = Converter.inchesToTicks(acceleration, RobotMap.Drivetrain.WHEEL_DIAMETER);
        this.endingVelocity = Converter.inchesToTicks(endingVelocity, RobotMap.Drivetrain.WHEEL_DIAMETER);
    }

    public DistancePathModifier setMatchVelocity(boolean matchVelocity) {
        this.matchVelocity = matchVelocity;

        return this;
    }

    @Override
    public TrajectoryPoint[] modifyPath(TrajectoryPoint[] points) {
        TrajectoryPoint[] path = this.generatePoints(
                points.length == 0 ? 0 : points[points.length - 1].velocity * 10,
                this.matchVelocity ? points[points.length - 1].velocity * 10 : this.velocity,
                this.endingVelocity,
                this.acceleration,
                this.distance
        );

        TrajectoryPoint[] newPoints = new TrajectoryPoint[points.length + path.length];

        System.arraycopy(points, 0, newPoints, 0, points.length);
        System.arraycopy(path, 0, newPoints, points.length, path.length);

        double startingPosition = newPoints[points.length - 1].position;

        for (int i = points.length; i < newPoints.length; i++) {
            newPoints[i].position += startingPosition;
        }

        this.correctTrajectoryPoints(newPoints);

        return newPoints;
    }

    private TrajectoryPoint[] generatePoints(double startVelocity, double cruiseVelocity, double endVelocity, double maxAcceleration, double distance) {
        startVelocity /= 10;
        cruiseVelocity /= 10;
        endVelocity /= 10;
        maxAcceleration /= 100;

        int s1 = cruiseVelocity >= startVelocity ? 1 : -1;
        int s2 = endVelocity >= cruiseVelocity ? 1 : -1;

        double c1 = Math.abs(cruiseVelocity - startVelocity) / maxAcceleration;
        double c2 = distance / cruiseVelocity + c1 * (1 - startVelocity / cruiseVelocity) / 2 - Math.abs(cruiseVelocity - endVelocity) * (cruiseVelocity + endVelocity) / 2 / maxAcceleration / cruiseVelocity;
        double c3 = c2 + Math.abs(endVelocity - cruiseVelocity) / maxAcceleration;

        if (c2 < c1) {
            throw new IllegalArgumentException("Not possible.");
        }

        int steps = (int) Math.ceil(5 * c3);

        TrajectoryPoint points[] = new TrajectoryPoint[steps];
        for (int i = 0; i < steps; ++i) {
            points[i] = new TrajectoryPoint();

            points[i].timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;
            if (i < 5 * c1) {
                points[i].velocity = startVelocity + s1 * maxAcceleration * i / 5.0;
                points[i].position = i * (startVelocity + s1 * maxAcceleration * i / 10.0) / 5.0;
            } else if (i < 5 * c2) {
                points[i].velocity = cruiseVelocity;
                points[i].position = c1 * (startVelocity + s1 * maxAcceleration * c1 / 2.0) + cruiseVelocity * (i / 5.0 - c1);
            } else {
                points[i].velocity = cruiseVelocity + s2 * maxAcceleration * (i / 5.0 - c2);
                points[i].position = c1 * (startVelocity + s1 * maxAcceleration * c1 / 2.0) + cruiseVelocity * (i / 5.0 - c1) + s2 * maxAcceleration * Math.pow(i / 5.0 - c2, 2) / 2;
            }

            points[i].profileSlotSelect0 = PIDF.TALON_MOTIONPROFILE_SLOT;
            points[i].isLastPoint = i == steps - 1;
            points[i].zeroPos = i == 0;
        }

        this.correctTrajectoryPoints(points);
        return points;
    }

}

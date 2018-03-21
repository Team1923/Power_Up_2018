package org.usfirst.frc.team1923.robot.utils.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderJNI;
import jaci.pathfinder.Trajectory;

public class TankModifier {

    private Trajectory source;
    private Trajectory leftTrajectory;
    private Trajectory rightTrajectory;

    private boolean inverted;

    public TankModifier(Trajectory source) {
        this.source = source;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public TankModifier modify(double wheelbaseWidth) {
        Trajectory[] trajectories = PathfinderJNI.modifyTrajectoryTank(this.source, wheelbaseWidth);

        this.leftTrajectory = trajectories[0];
        this.rightTrajectory = trajectories[1];

        this.correctHeadings(this.leftTrajectory);
        this.correctHeadings(this.rightTrajectory);

        return this;
    }

    public Trajectory getLeftTrajectory() {
        return this.inverted ? this.invertTrajectory(this.rightTrajectory) : this.leftTrajectory;
    }

    public Trajectory getRightTrajectory() {
        return this.inverted ? this.invertTrajectory(this.leftTrajectory) : this.rightTrajectory;
    }

    protected Trajectory invertTrajectory(Trajectory trajectory) {
        trajectory = trajectory.copy();

        for (Trajectory.Segment segment : trajectory.segments) {
            segment.position = -segment.position;
            segment.velocity = -segment.velocity;
            segment.acceleration = -segment.acceleration;
            segment.jerk = -segment.jerk;
        }

        return trajectory;
    }

    private void correctHeadings(Trajectory trajectory) {
        if (trajectory.segments.length < 2) {
            return;
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
    }

}

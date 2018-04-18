package org.usfirst.frc.team1923.robot.utils.pathfinder.modifier;

import com.ctre.phoenix.motion.TrajectoryPoint;

public class TrimPathModifier extends PathModifier {

    private boolean beginning;

    private int segments;

    public TrimPathModifier(int segments) {
        this(segments, false);
    }

    public TrimPathModifier(int segments, boolean beginning) {
        if (segments <= 0) {
            throw new IllegalArgumentException("Segment count cannot be negative.");
        }

        this.beginning = beginning;
        this.segments = segments;
    }

    @Override
    public TrajectoryPoint[] modifyPath(TrajectoryPoint[] points) {
        if (points.length <= this.segments) {
            return new TrajectoryPoint[0];
        }

        TrajectoryPoint[] newPoints = new TrajectoryPoint[points.length - this.segments];
        System.arraycopy(
                points,
                this.beginning ? this.segments - 1 : 0,
                newPoints,
                0,
                newPoints.length
        );

        this.correctTrajectoryPoints(newPoints);
        return newPoints;
    }

}

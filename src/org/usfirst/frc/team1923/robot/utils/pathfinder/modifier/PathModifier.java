package org.usfirst.frc.team1923.robot.utils.pathfinder.modifier;

import com.ctre.phoenix.motion.TrajectoryPoint;

public abstract class PathModifier {

    public abstract TrajectoryPoint[] modifyPath(TrajectoryPoint[] points);

    protected void correctTrajectoryPoints(TrajectoryPoint[] points) {
        for (int i = 0; i < points.length; i++) {
            points[i].zeroPos = i == 0;
            points[i].isLastPoint = i == points.length - 1;
        }
    }

}

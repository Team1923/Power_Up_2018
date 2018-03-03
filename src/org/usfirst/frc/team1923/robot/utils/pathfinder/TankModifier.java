package org.usfirst.frc.team1923.robot.utils.pathfinder;

import jaci.pathfinder.Trajectory;

public class TankModifier extends jaci.pathfinder.modifiers.TankModifier {

    private boolean inverted;

    public TankModifier(Trajectory source) {
        super(source);
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    @Override
    public Trajectory getLeftTrajectory() {
        return this.inverted ? this.invertTrajectory(super.getRightTrajectory()) : super.getLeftTrajectory();
    }

    @Override
    public Trajectory getRightTrajectory() {
        return this.inverted ? this.invertTrajectory(super.getLeftTrajectory()) : super.getRightTrajectory();
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

}

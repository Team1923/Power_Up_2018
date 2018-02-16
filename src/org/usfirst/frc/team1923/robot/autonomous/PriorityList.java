package org.usfirst.frc.team1923.robot.autonomous;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.util.List;

public class PriorityList extends SendableBase implements Sendable {

    private List<String> priorityList;

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Priority List");
    }

}

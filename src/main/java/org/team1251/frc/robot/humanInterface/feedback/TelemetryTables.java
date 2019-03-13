package org.team1251.frc.robot.humanInterface.feedback;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class TelemetryTables {

    private final NetworkTable telemetryTable;
    private final NetworkTable sensorTable;
    private final NetworkTable stateTable;

    public NetworkTable getStateTable() {
        return stateTable;
    }

    public NetworkTable getTelemetryTable() {
        return telemetryTable;
    }

    public NetworkTable getSensorTable() {
        return sensorTable;
    }

    public TelemetryTables() {
        telemetryTable = NetworkTableInstance.getDefault().getTable("Telemetry");
        sensorTable = telemetryTable.getSubTable("Sensors"); // Data from sensors
        stateTable = telemetryTable.getSubTable("State"); // Information about the state of the robot
    }
}

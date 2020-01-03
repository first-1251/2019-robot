package org.team1251.frc.robotCore.humanInterface.output;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Container class that provides Network tables for telemetry data.
 *
 * Telemetry data is generalized into two groups:
 *   - "sensors": Direct representation of data coming in from sensors
 *   - "state": Interpretation of sensor data and/or software state information to represent the state of the
 *              Robot (or some part of the robot)
 *
 * For example, whether or not a bumper switch is reporting as being pressed is sensor data but an indicator of
 * whether or not the robot has collected a game piece is state data.
 */
public class TelemetryTables {

    /**
     * The top-level table for all telemetry data. Sensor and state tables are each a sub-table of this.
     */
    private final NetworkTable telemetryTable;

    /**
     * The network table used to represent sensor data.
     */
    private final NetworkTable sensorTable;

    /**
     * The network table used to represent state data.
     */
    private final NetworkTable stateTable;

    /**
     * The main telemetry table. You usually do not need to get this.
     *
     * The state and sensor tables are sub-tables of this table. Generally, you will want to access the more specific
     * "state" or "sensor" table.
     *
     * @return A network table that contains telemetry sub-tables.
     */
    public NetworkTable getTelemetryTable() {
        return telemetryTable;
    }

    /**
     * Use this to record state data.
     *
     * Typically, a sub-table will be used to logically group the state data (for example "Elevator") instead of writing
     * directly to this table.
     *
     * State data represents an interesting software or hardware state - for example, whether or not the robot has
     * collected a game piece.
     *
     * @return A network table suitable for writing state data to.
     */
    public NetworkTable getStateTable() {
        return stateTable;
    }

    /**
     * Use this to record sensor data.
     *
     * Typically, a sub-table will be used to logically group the sensor data (for example "Elevator") instead
     * of writing directly to this table.
     *
     * For example, the distance or ticks being reported by an encoder.
     *
     * @return A network table suitable for writing state data to.
     */
    public NetworkTable getSensorTable() {
        return sensorTable;
    }


    public TelemetryTables() {
        telemetryTable = NetworkTableInstance.getDefault().getTable("Telemetry");
        sensorTable = telemetryTable.getSubTable("Sensors"); // Data from sensors
        stateTable = telemetryTable.getSubTable("State"); // Information about the state of the robot
    }
}

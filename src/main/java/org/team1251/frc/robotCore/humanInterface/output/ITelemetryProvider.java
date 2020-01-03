package org.team1251.frc.robotCore.humanInterface.output;

/**
 * Attached to classes that are capable of sending telemetry data to Network Tables.
 */
public interface ITelemetryProvider {
    /**
     * Sends the telemetry data.
     */
    void sendTelemetryData(TelemetryTables networkTables);
}

package org.team1251.frc.robotCore.humanInterface.feedback;

import java.util.ArrayList;

/**
 * A Telemetry Provider implementation that simply delegates to a list of other Telemetry Providers
 */
public class TelemetryProviderList extends ArrayList<ITelemetryProvider> implements ITelemetryProvider {

    /**
     * Send the telemetry.
     *
     * @param networkTables - The network tables that each ITelemetryProvider should write its data to.
     */
    public void sendTelemetryData(TelemetryTables networkTables) {
        // Loop over each provider, and send its data.
        for (ITelemetryProvider provider:this) {
            provider.sendTelemetryData(networkTables);
        }
    }
}

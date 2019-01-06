package org.team1251.frc.robot.robotMap;

import edu.wpi.first.wpilibj.Victor;
import org.team1251.frc.robotCore.robotMap.AbstractDeviceManager;
import org.team1251.frc.robotCore.robotMap.PortType;

/**
 * Class for centralized management of devices and their port assignments.
 */
public class DeviceManager extends AbstractDeviceManager<DeviceConnector> {
    /**
     * Create a new instance.
     */
    public DeviceManager() {
        super(DeviceConnector.class);
    }

    // TODO: Add Factory methods for devices (see createVictor() example below)

    /**
     * Create a instance of the standard Victor speed controller.
     */
    public Victor createVictor(DeviceConnector connector) {
        occupyPort(connector);
        return new Victor(getPortNumber(connector, PortType.PWM));
    }
}

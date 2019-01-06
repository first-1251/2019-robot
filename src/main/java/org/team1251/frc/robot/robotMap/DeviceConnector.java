package org.team1251.frc.robot.robotMap;

import org.team1251.frc.robotCore.robotMap.DeviceConnectorInterface;
import org.team1251.frc.robotCore.robotMap.Port;
import org.team1251.frc.robotCore.robotMap.PortType;


/**
 * List all devices (sensors, actuators, motor controllers, etc) in this enum.
 */
public enum DeviceConnector implements DeviceConnectorInterface {

    // Replace with devices for your robot
    SAMPLE_SWITCH(PortType.DIO, 0);

    /**
     * Port assignment for each Device
     */
    private final Port port;

    /**
     * @param portType The port type that the device is attached to
     * @param port The port the device is attached to.
     */
    DeviceConnector(PortType portType, int port) {
        this.port = new Port(portType, port);
    }

    @Override
    public Port getPort() {
        return port;
    }
}
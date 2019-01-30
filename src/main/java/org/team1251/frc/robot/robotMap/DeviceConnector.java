package org.team1251.frc.robot.robotMap;

import org.team1251.frc.robotCore.robotMap.DeviceConnectorInterface;
import org.team1251.frc.robotCore.robotMap.Port;
import org.team1251.frc.robotCore.robotMap.PortType;


/**
 * List all devices (sensors, actuators, motor controllers, etc) in this enum.
 */
public enum DeviceConnector implements DeviceConnectorInterface {

    /**
     * CAN port 0 is reserved for the PDP.
     *
     * From the docs (https://wpilib.screenstepslive.com/s/currentCS/m/java/l/219414-power-distribution-panel):
     *
     *    >"To work with the current versions of C++ and Java WPILib, the CAN ID for the PDP must be 0."
     */
    PDP_CAN(PortType.CAN, 0),

    MC_DRIVE_LEFT_BOTTOM(PortType.CAN, 1), // Talon
    MC_DRIVE_LEFT_TOP_FRONT(PortType.CAN, 2), // Victor
    MC_DRIVE_LEFT_TOP_REAR(PortType.CAN, 3), // Victor

    MC_DRIVE_RIGHT_BOTTOM(PortType.CAN, 4), // Talon
    MC_DRIVE_RIGHT_TOP_FRONT(PortType.CAN, 5), // Victor
    MC_DRIVE_RIGHT_TOP_REAR(PortType.CAN, 6); // Victor

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
package org.team1251.frc.robot.robotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Talon;
import org.team1251.frc.robotCore.robotMap.AbstractDeviceManager;
import org.team1251.frc.robotCore.robotMap.PortType;

/**
 * Class for centralized management of devices and their port assignments.
 *
 * All device instances should be created via factory methods provided by this class. Those factory methods will
 * "occupy" the requested device ports and throw a meaningful error if a given port is double-occupied.
 */
public class DeviceManager extends AbstractDeviceManager<DeviceConnector> {

    private PowerDistributionPanel pdpInstance;

    /**
     * Create a new instance.
     */
    public DeviceManager() {
        super(DeviceConnector.class);

        // Immediately occupy port 0 of can. It is reserved for the PDP.
        occupyPort(DeviceConnector.PDP_CAN);
    }

    // TODO: Add Factory methods for devices (see createTalon() example below)

    /**
     * Create a instance of the standard Victor speed controller.
     */
    public Talon createTalon(DeviceConnector connector) {
        occupyPort(connector);
        return new Talon(getPortNumber(connector, PortType.PWM));
    }

    public WPI_TalonSRX createTalonSRX(DeviceConnector connector) {
        occupyPort(connector);
        return new WPI_TalonSRX(getPortNumber(connector, PortType.CAN));
    }

    public WPI_VictorSPX createVictorSPX(DeviceConnector connector) {
        occupyPort(connector);
        return new WPI_VictorSPX(getPortNumber(connector, PortType.CAN));
    }

    public PowerDistributionPanel getPDP() {
        // Special handling for the PDP. Only ever create a single instance and always return that instance.
        if (pdpInstance != null) {
            return pdpInstance;
        }

        return pdpInstance = new PowerDistributionPanel();
    }
}

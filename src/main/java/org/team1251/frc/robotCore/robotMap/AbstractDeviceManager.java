package org.team1251.frc.robotCore.robotMap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The DeviceManager interacts with the Enum of devices in useful ways.
 *
 * To use it, you must first declare an `Enum` which implements {@link DeviceConnectorInterface}:
 *
 * ```
 *     public enum DeviceConnector implements DeviceConnectorInterface {
 *         // ...
 *     }
 *
 * That enum can then be used as the Generic type of your concrete DeviceManager:
 *
 * ```
 *     class DeviceManager extends AbstractDeviceManager<DeviceConnector> {
 *         public DeviceManager() {
 *             super(DeviceConnector.class);
 *         }
 *     }
 *
 *```
 *
 * Your concrete class should also define a variety of factory methods which create devices using device connectors.
 * Whenever you create a new device, you must be sure to occupy the port(s) used by the related device connectors:
 *
 * ```
 *     public void createVictor(DeviceConnector deviceConnector) {
 *         // Try to occupy the port. This will throw an exception with a meaningful message if the port is
 *         // already occupied by another connector.
 *         occupyPort(deviceConnector);
 *
 *         // Now use `getPortNumber()` to make sure we are attaching to the expected port type.
 *         return new Victor(getPortNumber(deviceConnector, PortType.PWM));
 *     }
 *```
 *
 * @param <E> The Enum which represents the list of devices. The Enum must implement the Device interface.
 */
public abstract class AbstractDeviceManager<E extends Enum<E> & DeviceConnectorInterface> {

    /**
     * The Enum Class which represents the list of deviceConnectors
     */
    private final Class<E> clazz;
    private final Map<Port, E> occupied = new HashMap<>();

    /**
     * Create a new instance.
     *
     * @param clazz The Enum Class which represents the list of deviceConnectors
     */
    protected AbstractDeviceManager(Class<E> clazz) {
        this.clazz = clazz;
        this.detectDuplicates();
    }

    /**
     * Iterates over the values of the given Enum class and throws an exception if any two values have equivalent
     * assignments.
     */
    private void detectDuplicates() throws IllegalArgumentException {

        // Make sure there are no duplicate assignments.
        Map<Port, E> assignments = new HashMap<>();
        for (E deviceConnector:clazz.getEnumConstants()) {

            // Look for an existing deviceConnector using this assignment.
            E existing = assignments.get(deviceConnector.getPort());
            if (existing != null) {
                // Assignment is already occupied!
                throw new IllegalArgumentException(
                        "Duplicate assignment! Can not assign " + deviceConnector.getPort().toString() +
                                " to " + deviceConnector.toString() + " --" +
                                " it is already assigned to " + existing.toString()
                );
            }

            // Not in use, so keep track of it to detect future duplicates
            assignments.put(deviceConnector.getPort(), deviceConnector);
        }
    }

    /**
     * Get the assigned port for a deviceConnector, restricted by port type.
     *
     * This makes it easy to get the port type you are expecting. Using it can help identify logical errors when
     * initializing deviceConnectors.
     *
     * This should be used in every device factory method:
     *
     * ```
     *     public Victor createVictor(E deviceConnector) {
     *         occupyPort(deviceConnector);
     *         return new Victor(getPortNumber(deviceConnector, PortType.PWM));
     *     }
     * ```
     *
     * @param deviceConnector The deviceConnector to get the assignment for.
     * @param expectedPortType The expected port type. If there is a mismatch between this type and the actual type
     *                         of the assignment, a `NoSuchElementException` will be thrown.
     *
     * @return The port number of the deviceConnector assignment.
     */
    protected final int getPortNumber(E deviceConnector, PortType expectedPortType) {
        if (deviceConnector.getPort().getPortType() != expectedPortType) {
            throw new NoSuchElementException(
                    "No " + expectedPortType.toString() + " port assignment available for " + deviceConnector.toString() + ", " +
                            "only " + deviceConnector.getPort().getPortType().toString());
        }

        return deviceConnector.getPort().getPortNumber();
    }

    /**
     * Occupies a port with a deviceConnector. Only one deviceConnector may use any given port assignment.
     *
     * This should be called by every factory method:
     *
     * ```
     *     public Victor createVictor(E deviceConnector) {
     *         occupyPort(deviceConnector);
     *         return new Victor(getPortNumber(deviceConnector, PortType.PWM));
     *     }
     * ```
     *
     * @throws IllegalArgumentException Thrown if a `NULL` deviceConnector is given or if another deviceConnector has
     *   already occupied the port.
     */
    protected final void occupyPort(E deviceConnector) throws IllegalArgumentException {
        if (deviceConnector == null) {
            throw new IllegalArgumentException("deviceConnector argument is not nullable");
        }

        Port port = deviceConnector.getPort();
        if (occupied.containsKey(port)) {
            throw new IllegalArgumentException(port.toString() + " already occupied by" + occupied.get(port).name());
        }

        occupied.put(port, deviceConnector);
    }
}
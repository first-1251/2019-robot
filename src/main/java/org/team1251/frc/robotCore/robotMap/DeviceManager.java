package org.team1251.frc.robotCore.robotMap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The DeviceManager interacts with the Enum of devices in useful ways.
 *
 * To use it, you must first declare an Enum of your devices that implements the Device interface and use it
 * as the Type Parameter for this Generic. A full example is available in the `RobotMapSample` class.
 *
 * @param <E> The Enum which represents the list of devices. The Enum must implement the Device interface.
 */
public class DeviceManager<E extends Enum<E> & PortAssignable> {

    /**
     * The Enum Class which represents the list of devices
     */
    private final Class<E> clazz;

    /**
     * Create a new instance.
     *
     * @param clazz The Enum Class which represents the list of devices
     */
    public DeviceManager(Class<E> clazz) {
        this.clazz = clazz;
    }

    /**
     * Iterates over the values of the given Enum class and throws an exception if any two values have equivalent
     * assignments.
     */
    public void detectDuplicates() throws IllegalArgumentException {

        // Make sure there are no duplicate assignments.
        Map<Assignment, E> assignments = new HashMap<>();
        for (E device:clazz.getEnumConstants()) {

            // Look for an existing device using this assignment.
            E existing = assignments.get(device.getAssignment());
            if (existing != null) {
                // Assignment is already occupied!
                throw new IllegalArgumentException(
                        "Duplicate assignment! Can not assign " + device.getAssignment().toString() +
                                " to " + device.toString() + " --" +
                                " it is already assigned to " + existing.toString()
                );
            }

            // Not in use, so keep track of it to detect future duplicates
            assignments.put(device.getAssignment(), device);
        }
    }

    /**
     * Get the assigned port for a device, restricted by port type.
     *
     * This makes it easy to get the port type you are expecting. Using it can help identify logical errors when
     * initializing devices.
     *
     * @param device The device to get the assignment for.
     * @param expectedPortType The expected port type. If there is a mismatch between this type and the actual type
     *                         of the assignment, a `NoSuchElementException` will be thrown.
     *
     * @return The port number of the device assignment.
     */
    public int getPort(E device, PortType expectedPortType) {
        if (device.getAssignment().getPortType() != expectedPortType) {
            throw new NoSuchElementException(
                    "No " + expectedPortType.toString() + " port assignment available for " + device.toString() + ", " +
                            "only " + device.getAssignment().getPortType().toString());
        }

        return device.getAssignment().getPort();
    }
}
package org.team1251.frc.robotCore.robotMap;

import java.util.Objects;

public class Port {

    private final PortType type;
    private final int portNumber;

    public Port(PortType type, int portNumber) {
        // Make sure we haven't violated the boundaries of the device type.
        if (portNumber < 0 || portNumber > type.maxPort) {
            throw new IllegalArgumentException(
                    "Invalid ID for " + type.name() + " device - must be between 0 and " + type.maxPort);
        }

        this.type = type;
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public PortType getPortType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        // See if it is the same object
        if (this == o)
            return true;

        // See if it is null
        if (o == null)
            return false;

        // Make sure it is the same exact type
        if (getClass() != o.getClass())
            return false;

        // Cast it and compare individual fields
        Port port = (Port) o;
        return Objects.equals(this.portNumber, port.portNumber)
                && Objects.equals(type, port.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, portNumber);
    }

    @Override
    public String toString() {
        return type.toString() + "(" + portNumber + ")";
    }
}
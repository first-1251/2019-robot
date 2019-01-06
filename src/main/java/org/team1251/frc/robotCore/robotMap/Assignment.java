package org.team1251.frc.robotCore.robotMap;

import java.util.Objects;

public class Assignment {

    private final PortType type;
    private final int port;

    public Assignment(PortType type, int port) {
        // Make sure we haven't violated the boundaries of the device type.
        if (port < 0 || port > type.maxPort) {
            throw new IllegalArgumentException(
                    "Invalid ID for " + type.name() + " device - must be between 0 and " + type.maxPort);
        }

        this.type = type;
        this.port = port;
    }

    public int getPort() {
        return port;
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
        Assignment assignment = (Assignment) o;
        return Objects.equals(port, assignment.port)
                && Objects.equals(type, assignment.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, port);
    }

    @Override
    public String toString() {
        return type.toString() + "(" + port + ")";
    }
}
package org.team1251.frc.robotCore.humanInterface.input;

public interface AnalogButtonConfig {

    /**
     * Provides a "dead zone" to be ignored.
     *
     * The standard use of the dead zone is to ignore all values up to and including the dead zone. Values outside of
     * the dead-zone are then "stretched" to fill the full space between 0 and 1 (or -1).
     *
     * Implementations must always provide a positive number.
     *
     * @return Values up to this number are to be ignored when reading the button value.
     */
    double getDeadZone();

    /**
     * The value between 0 and 1 at which the button is considered "pressed".
     *
     * The standard use of this value is to consider the button "pressed" once it has reached this value without
     * consideration for the dead-zone.
     *
     * @return The value at which the button should be considered "pressed"
     */
    double getPressedThreshold();
}

package org.team1251.frc.robotCore.humanInterface.input;

/**
 * Provides configuration for a stick-type input element.
 */
public interface StickConfig {

    /**
     * Provides a "dead zone" which applies outward from the center in all directions.
     *
     * The standard use of the dead zone is to ignore all values up to and including the dead zone (the dead zone is
     * negated when working with values between 0 and -1). Values outside of the dead-zone are then "stretched" to fill
     * the full space between 0 and 1 (or -1).
     *
     * Implementations must always provide a positive number.
     *
     * @return Values up to this number are to be ignored when reading the stick values.
     */
    double getDeadZone();

    /**
     * Indicates whether or not the horizontal axis values should be inverted. (e.g. treat left as right and right as left)
     *
     * @return Returns `true` if the horizontal access is inverted or `false` if it is in its "natural" state.
     */
    boolean isHorizontalInverted();

    /**
     * Indicates whether or not the horizontal axis values should be inverted. (e.g. treat left as right and right as left)
     *
     * @return Returns `true` if the horizontal access is inverted or `false` if it is in its "natural" state.
     */
    boolean isVerticalInverted();
}

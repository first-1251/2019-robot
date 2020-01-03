package org.team1251.frc.robotCore.humanInterface.input;

public class StickConfig {

    private final double deadZone;
    private final boolean isHorizontalInverted;
    private final boolean isVerticalInverted;

    /**
     *
     * @param deadZone Positive input values up to and including this value are ignored. Negative input values down to
     *                 and including the negation of this value are ignored. Useful for handling tolerances in the
     *                 resting position of the stick.
     * @param isHorizontalInverted Indicates whether or not the horizontal axis values should be inverted.
     * @param isVerticalInverted Indicates whether or not the vertical axis values should be inverted.
     */
    public StickConfig(double deadZone, boolean isHorizontalInverted, boolean isVerticalInverted) {
        this.deadZone = deadZone;
        this.isHorizontalInverted = isHorizontalInverted;
        this.isVerticalInverted = isVerticalInverted;
    }

    /**
     * Provides a "dead zone" which applies outward from the center in all directions.
     *
     * When working with a positive input range (i.e. 1 through 0), all values up to and including the dead zone
     * should be ignored and values outside of the dead-zone should be "stretched" to fill the full space between
     * 0 and 1.
     *
     * When working with a negative input range (i.e. -1 through 0), all values down to and including the negated
     * dead-zone should be ignored and values outside of the dead-zone range should be "stretched" to fill the full
     * space between -1 and 0.
     *
     * @return Values up to this number are to be ignored when reading the stick values.
     */
    public double getDeadZone() {
        return deadZone;
    }

    /**
     * Indicates whether or not the horizontal axis values should be inverted. (e.g. treat left as right and right as left)
     *
     * @return Returns `true` if the horizontal access is inverted or `false` if it is in its "natural" state.
     */
    public boolean isHorizontalInverted() {
        return isHorizontalInverted;
    }

    /**
     * Indicates whether or not the horizontal axis values should be inverted. (e.g. treat left as right and right as left)
     *
     * @return Returns `true` if the horizontal access is inverted or `false` if it is in its "natural" state.
     */
    public boolean isVerticalInverted() {
        return isVerticalInverted;
    }
}

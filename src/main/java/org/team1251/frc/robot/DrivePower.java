package org.team1251.frc.robot;

/**
 * Represents the left and right power to be applied to the drive base.
 */
public class DrivePower {
    /**
     * The amount of power to apply to the left side of the drive base.
     */
    private final double left;

    /**
     * The amount of power to apply to the right side of the drive base.
     */
    private final double right;

    /**
     * Creates a new instance.
     *
     * The drive base will only respect a power range of -1..1 (inclusive); values outside of that range will be
     * still be captured, without modification, by this class. This is more efficient than clamping and it can be
     * useful to see raw calculated values when debugging.
     *
     * @param left The amount of power to apply to the left side of the drive base.
     * @param right The amount of power to apply to the left side of the drive base.
     */
    public DrivePower(double left, double right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Provides the amount of power to apply to the left of the drive base.
     *
     * @return The amount of power to apply to the left side of the drive base.
     */
    public double getLeft() {
        return left;
    }

    /**
     * Provides the amount of power to apply to the right of the drive base.
     *
     * @return The amount of power to apply to the left side of the drive base.
     */
    public double getRight() {
        return right;
    }
}

package org.team1251.frc.robotCore.util;

import edu.wpi.first.wpilibj.Timer;

public class ValueSmoother {

    private final Timer timer;
    private final double idleTimeout;
    /**
     * A record of past values used to smooth out the latest value.
     */
    private double[] smoothing;

    /**
     * If true, then a change in sign between two contiguous samples will reset
     */
    private final boolean resetOnSignChange;

    /**
     * Convenience copy of the last received value. Saves the effort of digging it out of the
     * smoothing array.
     */
    private double lastRawValue;

    /**
     * Convenience copy of the last smoothed value. Saves the effort of repeating the sample math.
     */
    private double lastSmoothedValue;

    /**
     * The number of past values to use to smooth out the current value.
     */
    private final int numSamples;

    /**
     *
     * @param numSamples The number of samples to apply
     * @param resetOnSignChange If true, all past samples will be discarded if the value has changed sign since
     *                          the last sample.
     */
    public ValueSmoother(int numSamples, double idleTimeout, boolean resetOnSignChange) {
        timer = new Timer();
        timer.start();

        this.idleTimeout = idleTimeout;
        this.numSamples = numSamples;
        this.smoothing = new double[numSamples];
        this.resetOnSignChange = resetOnSignChange;
    }

    /**
     * Takes in a new value and returns the resulting smoothed value.
     */
    public double getSmoothedValue(double newValue) {

        // Check to see if the previous samples are still valid. They are discarded if they are stale or if the
        // sign has changed since the last sample and the resetOnSignChange flag is set.
        if (timer.get() > idleTimeout || (resetOnSignChange && hasSignChanged(newValue))) {
            // Re-initialize the sample list.
            smoothing = new double[numSamples];
        }

        timer.reset();
        return this.applySmoothing(newValue);
    }

    /**
     * Applies smoothing to the current stick value, adding the current value to the history of recent reads.
     *
     * @param currentValue The current stick reading.
     *
     * @return The current stick value smoothed over past readings.
     */
    private double applySmoothing(double currentValue) {



        double sum = 0.0;
        for (int i = 0; i < numSamples; i++) {
            // See if this is the last iteration.
            if (i < numSamples - 1) {
                // Replace this value with the one after it. If this the first iteration, the existing
                // value at this index is discarded. All others are preserved by the shifting that occurred
                // in the previous iteration.
                smoothing[i] = smoothing[i + 1];
            } else {
                // We've reached the last iteration. Nothing more to shift. The new value becomes the last element
                // (or the "newest") element of the array.
                smoothing[i] = currentValue;
            }

            // Aggregate the value as we pass by so that we don't need to loop again.
            sum += smoothing[i];
        }

        lastRawValue = currentValue;
        return lastSmoothedValue = sum / numSamples;
    }

    private boolean hasSignChanged(double currentValue) {
        return (lastSmoothedValue < 0 && currentValue > 0) || (lastSmoothedValue > 0 && currentValue < 0);
    }
}
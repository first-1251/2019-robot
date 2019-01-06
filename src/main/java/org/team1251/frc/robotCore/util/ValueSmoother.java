package org.team1251.frc.robotCore.util;

public class ValueSmoother {

    /**
     * A record of past values used to smooth out the latest value.
     */
    private double[] smoothing;

    /**
     * The number of past values to use to smooth out the current value.
     */
    private final int numSamples;

    /**
     *
     * @param numSamples The number of samples to apply
     */
    public ValueSmoother(int numSamples) {
        this.numSamples = numSamples;
        this.smoothing = new double[numSamples];
    }

    /**
     * Takes in a new value and returns the resulting smoothed value.
     *
     * @param newValue
     * 
     * @return
     */
    public double getSmoothedValue(double newValue) {
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
            if (i < numSamples - 1) {
                smoothing[i] = smoothing[i + 1];
                sum += smoothing[i];
            } else {
                smoothing[i] = currentValue;
                sum += currentValue;
            }
        }

        return sum / numSamples;
    }
}

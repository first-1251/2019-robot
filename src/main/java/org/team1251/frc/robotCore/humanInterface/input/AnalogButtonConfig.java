package org.team1251.frc.robotCore.humanInterface.input;

public class AnalogButtonConfig {

    private final double deadZone;
    private final double pressedThreshold;

    /**
     *
     * @param deadZone Maximum input value of the dead zone. The dead zone is a range of input values to ignore and
     *                 is useful for handling tolerances in the buttons "neutral" position. This value is clamped to
     *                 a range of 0-1.
     * @param pressedThreshold The input value at which the button should be considered to be "pressed". Useful when
     *                         reading the analog button as simply "pressed" or "not pressed". The deadZone does not
     *                         influence this value. This value is clamped to a a range of 0-1.
     */
    public AnalogButtonConfig(double deadZone, double pressedThreshold) {
        this.deadZone = Math.min(Math.max(0, deadZone), 1);
        this.pressedThreshold = Math.min(Math.max(0, pressedThreshold), 1);
    }

    /**
     * Maximum input value of the dead zone.
     *
     * The dead zone is a range of input values to ignore. This is useful for handling tolerances in the button's
     * "neutral" position.
     *
     * @return A positive number between 0 and 1. Input values at or below this value are ignored.
     */
    public double getDeadZone() {
        return deadZone;
    }

    /**
     * The input value at which the button should be considered to be "pressed".
     *
     * Useful when reading the analog button as simply "pressed" or "not pressed". The deadZone does not influence
     * this value.
     *
     * @return The input value at which the button should be considered "pressed"
     */
    public double getPressedThreshold() {
        return pressedThreshold;
    }
}

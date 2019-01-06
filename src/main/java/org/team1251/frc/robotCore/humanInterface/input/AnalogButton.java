package org.team1251.frc.robotCore.humanInterface.input;


/**
 * Represents a button which provides an analog value between 0 and 1.
 *
 * An analog button can also simply be interpreted as being "on" or "off".
 */
public interface AnalogButton extends Button {

    /**
     * A value between 0 and 1 as reported by the button. The higher the value, the further the button is being
     * depressed. This is the value directly from the button and is not subject to any implementation-specific
     * manipulation such as application of a dead-zone. This is usually not the value you want for controlling the
     * robot but may be interesting to report on.
     *
     * @return The current value being reported by the button. This will be a number between 0 and 1.
     */
     double getRawValue();

    /**
     * A value between 0 and 1 as reported by the button. The higher the value, the further the button is being
     * depressed. This value is subject to any implementation-specific manipulation such as application of a dead-zone.
     * This is usually the value you want for controlling the robot.
     *
     * @return The current value being reported by the trigger. This will be a number between 0 and 1.
     */
     double getValue();
}

package org.team1251.frc.robotCore.humanInterface.input;

/**
 * Represents a simple button with an "on and off" state.
 */
public interface Button {

    /**
     * Indicates whether or not this button is currently pressed.
     *
     * @return Returns `true` if the button is pressed or `false` if it is not.
     */
    boolean isPressed();
}

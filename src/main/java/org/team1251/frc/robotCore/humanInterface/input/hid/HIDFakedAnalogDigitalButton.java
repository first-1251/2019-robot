package org.team1251.frc.robotCore.humanInterface.input.hid;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.AnalogButton;

/**
 * A digital button from a GenericHID device that is pretending to be an analog button.
 */
public class HIDFakedAnalogDigitalButton implements AnalogButton {

    private final HIDButton digitalButton;

    /**
     *
     * @param rawDevice The raw HID device that contains this button.
     * @param buttonID The raw button to inspect to determine the trigger's simple "on/off" state.
     */
    public HIDFakedAnalogDigitalButton(GenericHID rawDevice, int buttonID) {
        this.digitalButton = new HIDButton(rawDevice, buttonID);
    }

    @Override
    public double getRawValue() {
        return this.isPressed() ? 1 : 0;
    }

    @Override
    public double getValue() {
        return this.getRawValue();
    }

    @Override
    public boolean isPressed() {
        return this.digitalButton.isPressed();
    }
}

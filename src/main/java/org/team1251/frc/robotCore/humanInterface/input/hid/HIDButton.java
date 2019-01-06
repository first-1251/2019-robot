package org.team1251.frc.robotCore.humanInterface.input.hid;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.Button;

/**
 * Represents a button which can be simply represented as "on" or "off".
 */
public class HIDButton implements Button {

    private final int buttonID;
    private final GenericHID rawDevice;

    /**
     *
     * @param rawDevice The raw HID device that contains this button.
     * @param buttonID The raw button to inspect to determine the trigger's simple "on/off" state.
     */
     public HIDButton(GenericHID rawDevice, int buttonID) {
        this.buttonID = buttonID;
         this.rawDevice = rawDevice;
     }

    @Override
    public boolean isPressed() {
        return rawDevice.getRawButton(this.buttonID);
    }
}

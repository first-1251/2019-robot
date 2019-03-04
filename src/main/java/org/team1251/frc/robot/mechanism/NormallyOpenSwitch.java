package org.team1251.frc.robot.mechanism;

import edu.wpi.first.wpilibj.DigitalInput;


/**
 * Interprets a digitalInput as a normally open switch (a.k.a "NO switch").
 *
 * With a normally open switch it is impossible to tell the difference between the switch being
 * inactive or the switch completely missing. In other words, a missing switch appears to be inactive.
 * `NO switches` are good for cases where you can safely operate the robot in absence of the switch.
 * For example, a switch which stops collecting once a game piece has been obtained is a good
 * candidate if the operator can visually detect that the game piece has been obtained and manually
 * stop collection.
 *
 * NOTE: This class is NOT interchangeable with `NormallyClosedSwitch`. You MUST use the one which
 *       corresponds to the physical switch.
 */
public class NormallyOpenSwitch {

    private final DigitalInput nativeInput;


    public NormallyOpenSwitch(int channel) {
        this.nativeInput = new edu.wpi.first.wpilibj.DigitalInput(channel);
    }

    public boolean isActive() {
        return !isInactiveOrAbsent();
    }

    public boolean isInactiveOrAbsent() {
        // Native DIO is normally HIGH, so a normally open switch appears the sae as an absent switch.
        return nativeInput.get();
    }
}

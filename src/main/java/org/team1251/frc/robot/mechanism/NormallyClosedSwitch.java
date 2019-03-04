package org.team1251.frc.robot.mechanism;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Interprets a digitalInput as a normally closed switch (a.k.a "NC switch").
 *
 * With a normally closed switch it is impossible to tell the difference between
 * the switch being active or the switch completely missing. In other words, a missing switch
 * appears to be active. NC switches are good for cases where you would rather a mechanism
 * does not run at all if its limit switch fails.
 *
 * NOTE: This class is NOT interchangeable with `NormallyOpenSwitch`. You MUST use the one which
 *       corresponds to the physical switch (or the wiring configuration for switches that support
 *       both `NC` and `NO` operation options)
 */
public class NormallyClosedSwitch {

    private final DigitalInput nativeInput;


    public NormallyClosedSwitch(int channel) {
        this.nativeInput = new edu.wpi.first.wpilibj.DigitalInput(channel);
    }

    public boolean isActiveOrAbsent() {
        // Native DIO is normally HIGH, so a normally closed switch reads LOW when inactive.
        return !isInactive();
    }

    public boolean isInactive() {
        // Native DIO is normally HIGH, so a normally closed switch reads LOW when inactive.
        return !nativeInput.get();
    }
}

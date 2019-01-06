package org.team1251.frc.robotCore.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * A WPI Lib trigger which is always true.
 */
public class Always extends Trigger
{
    @Override
    public boolean get() {
        return true;
    }
}

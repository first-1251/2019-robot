package org.team1251.frc.robot.humanInterface.input;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;

public class LongPressTrigger extends Button {

    private final edu.wpi.first.wpilibj.buttons.Button trigger;
    private final double duration;
    private boolean isCandidate = false;
    private final Timer timer;

    public LongPressTrigger(Button trigger, double duration) {
        this.duration = duration;
        this.trigger = trigger;
        timer = new Timer();
    }

    @Override
    public boolean get() {
        // If the wrapped trigger is not active, reset everything before returning false.
        if (!trigger.get()) {
            timer.stop();
            timer.reset();
            isCandidate = false;
            return false;
        }

        // The wrapped trigger is active. See if it was previously marked as a candidate (previously active)
        if (!isCandidate) {
            // Not previously a candidate. Make a candidate and and reset/start the timer.
            timer.reset();
            timer.start();
            isCandidate = true;
        }

        // In all cases, we are a candidate for being active. (Either was one previously, or just now became one).
        // If the configured duration has passed since we became a candidate, then we are active.
        return timer.get() >= duration;
    }
}

package org.team1251.frc.robotCore.humanInterface.input.triggers;

import org.team1251.frc.robotCore.humanInterface.input.Button;

/**
 * A WPI Lib Trigger based on any 1251 human-input button.
 */
public class ButtonTrigger extends edu.wpi.first.wpilibj.buttons.Button {

    private final Button button;

    public ButtonTrigger(Button button) {
        this.button = button;
    }

    @Override
    public boolean get() {
        return button.isPressed();
    }
}

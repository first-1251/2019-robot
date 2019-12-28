package org.team1251.frc.robotCore.humanInterface.input.triggers;

import org.team1251.frc.robotCore.humanInterface.input.Button;

public class DualButtonTrigger extends edu.wpi.first.wpilibj.buttons.Button {
    private final Button button1;
    private final Button button2;

    public DualButtonTrigger(Button button1, Button button2) {
        this.button1 = button1;
        this.button2 = button2;
    }

    @Override
    public boolean get() {
        return button1.isPressed() && button2.isPressed();
    }
}

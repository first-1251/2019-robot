package org.team1251.frc.robotCore.humanInterface.input.hid;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.AnalogButton;
import org.team1251.frc.robotCore.humanInterface.input.AnalogButtonConfig;

public class HIDAnalogButton implements AnalogButton {

    private final int axisID;
    private final GenericHID rawDevice;
    private final double deadZone;
    private final double pressedValue;

    public HIDAnalogButton(GenericHID rawDevice, int axisID, AnalogButtonConfig config) {
        this.rawDevice = rawDevice;
        this.axisID = axisID;
        this.deadZone = config.getDeadZone();
        this.pressedValue = config.getPressedThreshold();
    }

    @Override
    public boolean isPressed() {
        return this.getRawValue() >= this.pressedValue;
    }

    @Override
    public double getRawValue() {
        return rawDevice.getRawAxis(this.axisID);
    }

    @Override
    public double getValue() {
        return (this.getRawValue() - deadZone) / (1.0 - deadZone);
    }
}

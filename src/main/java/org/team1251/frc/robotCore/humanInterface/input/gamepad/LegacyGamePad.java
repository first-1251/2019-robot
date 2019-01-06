package org.team1251.frc.robotCore.humanInterface.input.gamepad;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.StickConfig;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDButton;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDFakedAnalogDigitalButton;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDStick;

public class LegacyGamePad extends GamePad {

    public LegacyGamePad(GenericHID rawDevice, StickConfig leftStickConfig, StickConfig rightStickConfig) {
        this.a = new HIDButton(rawDevice, 2);
        this.b = new HIDButton(rawDevice, 3);
        this.x = new HIDButton(rawDevice, 1);
        this.y = new HIDButton(rawDevice, 4);
        this.lb = new HIDButton(rawDevice, 5);
        this.rb = new HIDButton(rawDevice, 6);
        this.select = new HIDButton(rawDevice, 9);
        this.start = new HIDButton(rawDevice, 10);
        this.rsClick = new HIDButton(rawDevice, 12);
        this.lsClick = new HIDButton(rawDevice, 11);
        this.lt = new HIDFakedAnalogDigitalButton(rawDevice, 7);
        this.rt = new HIDFakedAnalogDigitalButton(rawDevice, 8);

        this.ls = new HIDStick(rawDevice, 0, 1, leftStickConfig);
        this.rs = new HIDStick(rawDevice, 2, 3, rightStickConfig);
    }

    @Override
    public void rumbleLeft(double value) {
        // No rumble support.
    }

    @Override
    public void rumbleRight(double value) {
        // No rumble support.
    }
}

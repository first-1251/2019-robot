package org.team1251.frc.robotCore.humanInterface.input.gamepad;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDAnalogButton;
import org.team1251.frc.robotCore.humanInterface.input.AnalogButtonConfig;
import org.team1251.frc.robotCore.humanInterface.input.StickConfig;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDButton;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDStick;

public class ModernGamePad extends GamePad {

    private final GenericHID rawDevice;

    public ModernGamePad(GenericHID rawDevice,
                         StickConfig leftStickConfig,
                         StickConfig rightStickConfig,
                         AnalogButtonConfig leftTriggerConfig,
                         AnalogButtonConfig rightTriggerConfig) {

        this.rawDevice = rawDevice;

        this.a = new HIDButton(rawDevice, 1);
        this.b = new HIDButton(rawDevice, 2);
        this.x = new HIDButton(rawDevice, 3);
        this.y = new HIDButton(rawDevice, 4);
        this.lb = new HIDButton(rawDevice, 5);
        this.rb = new HIDButton(rawDevice, 6);
        this.select = new HIDButton(rawDevice, 7);
        this.start = new HIDButton(rawDevice, 8);
        this.rsClick = new HIDButton(rawDevice, 10);
        this.lsClick = new HIDButton(rawDevice, 9);

        this.lt = new HIDAnalogButton(rawDevice, 2, leftTriggerConfig);
        this.rt = new HIDAnalogButton(rawDevice, 3, rightTriggerConfig);

        this.ls = new HIDStick(rawDevice, 0, 1, leftStickConfig);
        this.rs = new HIDStick(rawDevice, 4, 5, rightStickConfig);
    }

    @Override
    public void rumbleLeft(double value) {
        this.rawDevice.setRumble(GenericHID.RumbleType.kLeftRumble, value);
    }

    @Override
    public void rumbleRight(double value) {
        this.rawDevice.setRumble(GenericHID.RumbleType.kRightRumble, value);
    }
}

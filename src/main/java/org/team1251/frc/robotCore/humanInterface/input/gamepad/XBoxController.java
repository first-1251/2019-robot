package org.team1251.frc.robotCore.humanInterface.input.gamepad;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.AnalogButton;
import org.team1251.frc.robotCore.humanInterface.input.AnalogButtonConfig;
import org.team1251.frc.robotCore.humanInterface.input.Button;
import org.team1251.frc.robotCore.humanInterface.input.StickConfig;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDAnalogButton;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDButton;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDStick;

public class XBoxController {

    private final GenericHID rawDevice;
    Button a;
    Button b;
    Button x;
    Button y;
    Button rb;
    Button lb;
    Button rsClick;
    Button lsClick;
    Button start;
    Button select;
    AnalogButton lt;
    AnalogButton rt;
    HIDStick ls;
    HIDStick rs;

    /**
     * Create a new instance with default stick and trigger configurations.
     */
    public XBoxController(GenericHID rawDevice) {
        this(rawDevice,
                new StickConfig(.05, true, false),
                new StickConfig(.05, false, false),
                new AnalogButtonConfig(.05, .50),
                new AnalogButtonConfig(.05, .50)
        );
    }

    /**
     * Create a new instance with specific stick and trigger configurations
     */
    public XBoxController(GenericHID rawDevice,
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

    public void rumbleLeft(double value) {
        this.rawDevice.setRumble(GenericHID.RumbleType.kLeftRumble, value);
    }

    public void rumbleRight(double value) {
        this.rawDevice.setRumble(GenericHID.RumbleType.kRightRumble, value);
    }

    public Button a() {
        return a;
    }

    public Button b() {
        return b;
    }

    public Button x() {
        return x;
    }

    public Button y() {
        return y;
    }

    public Button rb() {
        return rb;
    }

    public Button lb() {
        return lb;
    }

    public Button lsClick() {
        return lsClick;
    }

    public Button rsClick() {
        return rsClick;
    }

    public Button start() {
        return start;
    }

    public Button select() {
        return select;
    }

    public AnalogButton lt() {
        return lt;
    }

    public AnalogButton rt() {
        return rt;
    }

    public HIDStick ls() {
        return ls;
    }

    public HIDStick rs() {
        return rs;
    }
}

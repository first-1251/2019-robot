package org.team1251.frc.robotCore.humanInterface.input.gamepad;


import org.team1251.frc.robotCore.humanInterface.input.AnalogButton;
import org.team1251.frc.robotCore.humanInterface.input.Button;
import org.team1251.frc.robotCore.humanInterface.input.hid.HIDStick;

/**
 * Abstract representation of a typical 12-button, dual-stick game pad.
 *
 * Examples:
 *
 *  // See if the 'A' button is pressed.
 *  driverPad.a().isPressed();
 *
 *  // See if both 'A' and 'B' are pressed.
 *  driverPad.a().isPressed() && driverPad.b().isPressed()
 *
 *  // See if the left trigger is pressed.
 *  driverPad.lt().isPressed();
 *
 *  // See how far the left trigger is being pressed
 *  driverPad.lt().getValue();
 *
 *  // Read the horizontal position of the left stick.
 *  driverPad.ls().getHorizontal();
 *
 */
abstract public class GamePad {
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

    abstract public void rumbleLeft(double value);
    abstract public void rumbleRight(double value);

}

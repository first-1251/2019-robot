package org.team1251.frc.robotCore.humanInterface.input.hid;

import edu.wpi.first.wpilibj.GenericHID;
import org.team1251.frc.robotCore.humanInterface.input.Stick;
import org.team1251.frc.robotCore.humanInterface.input.StickConfig;

/**
 * Represents a stick-type input element, such as a stick on a game pad.
 *
 * A Stick can report its x and y axis, either of which can be inverted. A dead-zone can also be applied which indicates
 * a value distance from the center which is to be ignored.
 */
public class HIDStick implements Stick {

    private final int verticalAxisID;
    private final int horizontalAxisID;
    private final GenericHID rawDevice;
    private final double deadZone;
    private final int horizontalModifier;
    private final int verticalModifier;

    /**
     * @param horizontalAxisID The raw axis to inspect to get the horizontal position of the stick.
     * @param verticalAxisID The raw axis to inspect to get the vertical position of the stick.
     * @param rawDevice The raw HID that contains this stick.
     * @param config Configuration for this stick. This config state is "unwrapped" upon receipt so changes to
     *               the config instance after this class is constructed will not implicitly affect the stick's
     *               behavior.
     */
    public HIDStick(GenericHID rawDevice, int horizontalAxisID, int verticalAxisID, StickConfig config) {
        this.verticalAxisID = verticalAxisID;
        this.horizontalAxisID = horizontalAxisID;
        this.rawDevice = rawDevice;
        this.deadZone = config.getDeadZone();
        this.horizontalModifier = config.isHorizontalInverted() ? -1 : 1;
        this.verticalModifier = config.isVerticalInverted() ? -1 : 1;
    }

    /**
     * Provides the current vertical position of the stick. This is the value directly from the device and is not
     * subject to the dead-zone or inversion modifier.
     *
     * This is usually not the value you want to use for controlling the robot, but may be interesting to report on.
     *
     * @return A value between -1 and 1 where 0 is the center position along the vertical axis. The value approaches 1
     *     as the stick is moved upwards and approaches -1 as it is moved downward.
     */
    public double getRawVertical() {
        return rawDevice.getRawAxis(verticalAxisID);
    }

    /**
     * Provides the current vertical position of the stick adjusted for the dead-zone and inversion modifier.
     *
     * This is usually the value you want to use for controlling the robot.
     *
     * @return A value between -1 and 1 where 0 is the center position along the vertical axis. In its natural state
     *     (e.g. not inverted) the value approaches 1 as the stick is moved upwards and approaches -1 as it is moved
     *     downward.
     */
    public double getVertical() {
        return applyDeadZone(getRawVertical() * verticalModifier);
    }

    /**
     * Provides the current horizontal position of the stick. This is the value directly from the device and is not
     * subject to the dead-zone or inversion modifier.
     *
     * This is usually not the value you want to use for controlling the robot, but may be interesting to report on.
     *
     * @return A value between -1 and 1 where 0 is the center position along the horizontal axis. The value approaches 1
     *     as the stick is moved upwards and approaches -1 as it is moved downward.
     */
    public double getRawHorizontal() {
        return rawDevice.getRawAxis(horizontalAxisID);
    }

    /**
     * Provides the current horizontal position of the stick adjusted for the dead-zone and inversion modifier.
     *
     * This is usually the value you want to use for controlling the robot.
     *
     * @return A value between -1 and 1 where 0 is the center position along the horizontal axis. In its natural state
     *     (e.g. not inverted) the value approaches 1 as the stick is moved rightwards and approaches -1 as it is moved
     *     leftward.
     */
    public double getHorizontal() {
        return applyDeadZone(getRawHorizontal() * horizontalModifier);
    }

    /**
     * A helper which will apply a deadZone against a given axis value. Any value within the dead zone (positive or
     * negative) will be ignored. Any value which extends beyond the dead zone will be stretched back out into a
     * value between 0 and 1.
     *
     * @param axisValue The axis value to apply the deadZone to.
     *
     * @return The adjusted axis value.
     */
    private double applyDeadZone(double axisValue) {

        // Lifted logic from `DifferentialDrive.applyDeadBand()`
        if (Math.abs(axisValue) > deadZone) {
            if (axisValue > 0.0) {
                return (axisValue - deadZone) / (1.0 - deadZone);
            } else {
                return (axisValue + deadZone) / (1.0 - deadZone);
            }
        } else {
            return 0.0;
        }
    }
}

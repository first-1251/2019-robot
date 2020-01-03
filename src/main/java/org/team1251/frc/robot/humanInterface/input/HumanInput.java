package org.team1251.frc.robot.humanInterface.input;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import org.team1251.frc.robot.DrivePower;
import org.team1251.frc.robot.commands.AbandonClimb;
import org.team1251.frc.robot.commands.Climb;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.XBoxController;
import org.team1251.frc.robotCore.humanInterface.input.triggers.ButtonTrigger;
import org.team1251.frc.robotCore.humanInterface.input.triggers.DualButtonTrigger;
import org.team1251.frc.robotCore.humanInterface.input.triggers.LongPressTrigger;

/**
 * The HumanInput encapsulates everything related to human input and provides a clean interface for all commands and
 * subsystems to use.
 *
 * All knowledge about which buttons/sticks do what is contained within this class -- no other code should be reading
 * directly from the driver input devices. By centralizing this knowledge, it becomes much easier to adjust the control
 * scheme since it is not scattered throughout the code base. This also uses "information hiding" to make sure that
 * the rest of the robot does care about the details of how driver input is interpreted.
 */
public class HumanInput {

    /**
     * Helper to get drive power from the human input.
     */
    private final HumanDriveInput humanDriveInput;
    private final Button climbLvl3Trigger;
    private final Button climbLvl2Trigger;
    private final DualButtonTrigger abandonClimbTrigger;


    /**
     * Indicates that command triggers have already been attached.
     */
    private boolean commandTriggersAttached = false;

    /**
     * The primary input device
     */
    private XBoxController driverPad;

    /**
     * Creates a new instance
     */
    public HumanInput() {
        driverPad = new XBoxController(new Joystick(0));

        humanDriveInput = new TigerDriveInput();

        climbLvl3Trigger = new LongPressTrigger(new ButtonTrigger(driverPad.start()), .25);
        climbLvl2Trigger = new LongPressTrigger(new ButtonTrigger(driverPad.select()), .25);

        abandonClimbTrigger = new DualButtonTrigger(driverPad.rb(), driverPad.lb());
    }

    /**
     * Grants direct access to the game pad to other classes within this package.
     *
     * This is useful in cases when the logic for deriving specific input values is better handled by more specific
     * classes (for example, classes for deriving drive power in various ways).
     *
     * @return The GamePad which the human uses to control the robot.
     */
    public XBoxController getDriverPad() {
        return driverPad;
    }


    /**
     * Call to attach human-input triggers to commands.
     *
     * NOTE: All commands should be added as parameters to this method.
     */
    public void attachCommandTriggers(
            Climb climbLvl3,
            Climb climbLvl2,
            AbandonClimb abandonClimb) {

        // This is the typical way to prevent duplicate bindings.
        if (commandTriggersAttached) {
            return;
        }
        commandTriggersAttached = true;

        // By Default, there is no reason to "remember" the commands or the triggers as class properties. But now
        // would be a reasonable time to do it, if you have a reason to.
        climbLvl3Trigger.whenPressed(climbLvl3);
        climbLvl2Trigger.whenPressed(climbLvl2);
        abandonClimbTrigger.whenPressed(abandonClimb);
    }

    /**
     * Provides the amount of power to apply to each of the drive trains according to the human input.
     *
     * @param leftVelocity The current velocity of the left drive train
     * @param rightVelocity the current velocity of the right drive train
     *
     * @return A `DrivePower` instance representing the power to be applied ot the left and right drive trains.
     */
    public DrivePower getDrivePower(double leftVelocity, double rightVelocity) {
        return humanDriveInput.getDrivePower(this, leftVelocity, rightVelocity);
    }
}
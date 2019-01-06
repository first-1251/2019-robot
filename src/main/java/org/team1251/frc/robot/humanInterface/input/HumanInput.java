package org.team1251.frc.robot.humanInterface.input;

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
     * Indicates that command triggers have already been attached.
     */
    private boolean commandTriggersAttached = false;

    /**
     * Creates a new instance
     */
    public HumanInput() {
        // TODO: Inject or instantiate human input devices, store them in local private properties.

    }

    /**
     * Attaches human-input related triggers to commands.
     *
     * This method should only ever be called once.
     *
     * ** WARNING **
     *
     * There is no built-in way to detach a command trigger. There is no built-in protection against
     * attaching two commands to the same trigger.
     */
    public void attachCommandTriggers() {

        // TODO: Inject commands which need to be attached to command triggers.

        // This the typical way to prevent duplicate bindings.
        if (commandTriggersAttached) {
            return;
        }
        commandTriggersAttached = true;

        // TODO: Create the command triggers (Hint: org.team1251.frc.robotCore.humanInput.triggers.ButtonTrigger)
        // TODO: Attach commands to the command triggers.

        // By Default, there is no reason to "remember" the commands or the triggers as class properties. But now
        // would be a reasonable time to do it, if you have a reason to.


    }

    // TODO: Implement public access to meaningful values, but NOT to the devices!
    // Good: `public double getElevatorSpeed() { return gamePad.getVertical(); }`
    // Bad:  `public GamePad getGamePad() { return gamePad; }
}
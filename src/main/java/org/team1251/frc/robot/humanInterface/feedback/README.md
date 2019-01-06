# robot.humanInterface.feedback

This package is used for classes related to providing feedback to the human(s). That feedback may be in the form of
on-screen data, lights, sounds, vibrations, etc.

## Using Subsystems

When dealing with human feedback, it sometimes make sense to create a `Subsystem` to govern the feedback device. 
This may seem weird at first because we normally associate a `Subsystem` as part of the Robot! But if we look at
key characteristics of a `Subsystem` within the _Command Based_ programming context, it starts to make sense:

  - `Commands` send instructions to a `Subsystem`
  - `Commands` are ran activated by a `Trigger` when the Trigger's condition has been met.
  - Only one `Command` may have control over a single `Subsystem` at any moment to protect the subsystem against
    conflicting instructions.
  - A `Command` may be given priority to a subsystem by making it _non-interruptable_; in this case, once it has
    control of a `Subsystem`, no other Command may take control until it finishes executing.

From that perspective, they fit pretty well. Imagine that you want to use a controller's rumble feature as a way to 
provide feedback to the driver. Your goals are:

  1. Enable 50% rumble whenever there is a game piece nearby
  2. Enable 100% rumble for 5 seconds whenever a game piece is successfully collected
   
Let's see Subsystems, Commands, and Triggers make sense...

> `Commands` send instructions to a `Subsystem`

We could make two commands to send the different rumble instructions:
  - `NotifyPieceNearbyCmd` (set rumble to 50%)
  - `NotifyPieceCollectedCmd` (set rumble to 100% for 5 seconds)
  
> `Commands` are ran activated by a `Trigger` when the Trigger's condition has been met.

We could have two triggers, one for each of the conditions and use them to activate the commands
  - `PieceNearbyTrigger` --> while active --> `NotifyPieceNearbyCmd`
  - `PieceCollectedDevice` --> when active --> `NotifyPieceCollectedCmd`
  
> Only one `Command` may have control over a single `Subsystem` at any moment to protect the subsystem against
  conflicting instructions.
  
That's useful! We don't want to send both 50% and 100% rumble instructions to the `DriverRumble` subsystem at the 
same time.

> A `Command` may be given priority to a subsystem by making it _non-interruptable_; in this case, once it has
  control of a `Subsystem`, no other Command may take control until it finishes executing.
  
Perfect! We can make it so that `NotifyPieceCollectedCmd` can't be interrupted so that it is always given 
priority. If a game piece is nearby _before_ the piece is collected, the `NotifyPieceCollectedCmd` would 
interrupt `NotifyPieceNearby` and take control of the `DriverRumble` subsystem.  However, the opposite is not true;
`NotifyPieceNearby` could not steal control away from `NotifyPieceCollectedCmd` until it has finished (5 seconds).

That looks like a fit!  Here's what the Subsystem might look like:

```java
/**
 * A subsystem governing the rumble of the driver's game pad.
 */
public class DriverRumble extends NoInitDefaultCmdSubsystem {
    private final GamePad gamePad;

    public Rumble(GamePad gamePad) {
        this.gamePad = gamePad;
    }

    public void setRumble(double value) {
        gamePad.rumbleLeft(value);
        gamePad.rumbleRight(value);
    }

    public void setRumble(double leftValue, double rightValue) {
        gamePad.rumbleLeft(leftValue);
        gamePad.rumbleRight(rightValue);
    }
}
```

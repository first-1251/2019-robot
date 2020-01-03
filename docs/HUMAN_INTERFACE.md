# Human Interface

Human interfaces are simply the  way that humans operate with the Robot. This includes information sent to the Robot
(input) and information received from the robot (output). 

Robot-specific Human Interface classes are created in the `org.team1251.frc.robot.humanInterface` package. 

## XBoxController

The `org.team1251.frc.robotCore.humanInterface.input.XBoxController` provides an easy-to-use interface to XBox 
(or compatible) controllers.

## Triggers

`Triggers` are used to activate `Commands`. The `org.team1251.frc.robotCore.humanInterface.input.triggers` package
provides some useful triggers for attaching commands to human input devices such as the 
[XBoxController](#xboxcontroller).

```java
// `collectPiece` is a command for collecting the game piece. We want to activate it when we press the "A" button on
// the XBox controller.
this.collectPieceTrigger = new ButtonTrigger(gamePad.a());
collectPieceTrigger.whenPressed(collectPiece));
```

If we want to change the button mapping to be "B" instead of A, it is a simple change.
```java
this.collectPieceTrigger = new ButtonTrigger(gamePad.b());
```

We can even require two buttons or a long-press!
```java
this.collectPieceTrigger = new DualButtonTrigger(gamePad.b(), gamePad.a()); // A and B
```
```java
this.collectPieceTrigger = new DualButtonTrigger(gamePad.a(), .5); // Hold "A" for .5 seconds
```

## HumanInput

The `HumanInput` class is standard for most Robots. It provides a central class for accessing human inputs.  


## All Human Input in One Place
 
Input should be collected by this class and then provided through meaningful methods. This makes the code easier to 
understand and safer to change.

Take this example of a `MoveElevator` command that reads directly from an xbox controller.
```java
class MoveElevator extends Command {
    // ...
    public MoveElevator(XboxController xboxController) {
      this.xboxController = xboxController;
    }

    @Override
    public void execute() {
       elevator.move(xboxController.leftStick().getVertical());
    }    
}
```

If you want to change that to be "right stick" instead of "left stick", you must modify the command. Then you must 
check other Command classes to make sure nothing is already using "left stick" so that you don't end up with a 
control conflict. This may not seem like a big deal, but it becomes harder to do without mistakes as your Robot
code grows. 

Now let's look at the same command, with `HumanInput` as the source for human inputs:
```java
class MoveElevator extends Command {
    // ...
    public MoveElevator(HumanInput humanInput) {
      this.humanInput = humanInput;
    }

    @Override
    public void execute() {
       elevator.move(humanInput.getElevatorPower());
    }    
}
``` 

In this case, `HumanInput` would look something like this:
```java
class HumanInput {
    // ...
    public HumanInput(XboxController operatorController) {
      this.operatorController = operatorController;
    }


    public double getElevatorPower() {
       return operatorController.leftStick().getVertical();
    }

}
```

Now, we can change the _source_ of the input without modifying the Command. Also, all of our other input sources are
located within the same `HumanInput` class, so it is much easier to find and resolve control conflicts.

### Attaching Command Triggers

`HumanInput` is also the preferred place to attach command triggers related to human input. As mentioned above, 
this centralizes all of the logic that reads human input making it easier and safer to make changes.

This is generally done with `attachCommandTriggers()` method that takes in ALL of the commands as arguments.

```java
class HumanInput {
  
    /**
     * The xbox controller being used for input by the operator player
     */
    private final XBoxController operatorController;


    public HumanInput(XboxController operatorController) {
        this.operatorController = operatorController;
    }
   
    public attachCommandTriggers(Command climb, Command raiseElevator) {
        // Create triggers first -- this makes it easy to change the button mapping with creating conflicts.
        ButtonTrigger raiseElevator = new ButtonTrigger(operatorController.a());
        DualButtonTrigger climbTrigger = new DualButtonTrigger(operatorController.start(), operatorController.back());
         
        // Attach triggers to the commands
        raiseElevatorTrigger.whileHeld(raiseElevator);
        climbTrigger.whenPressed(climb);
   }
}
```

Now all we have to do is call it! We (usually) generally don't need to attach human input until Teleop mode is 
activated, but we only want to do it on the FIRST activation. [`TigerTimedRobot`](TIGER_TIMED_ROBOT.md) to the rescue!

```java
class Robot extends TigerTimedRobot {
    
    /**
     * Single-source for all human input
     */ 
    private HumanInput humanInput;

    public Robot() {
        super(.02);
    }

    // ...
    @Override
    public void createHumanInterfaces() {
        // Get a handle to the raw input device on port 0 and then treat it as an Xbox controller
        GenericHID rawHID = new GenericHID(0);
        XBoxController operatorGamepad = new XboxController(rawHID);
    
        // Pass the controller in to a new HumanInput and store it in a field for later use.
        this.humanInput = new HumanInput(xboxController);
    }

   
    @Override
    public void onTeleopFirstActivation() {
        // ONLY on first activation of teleop!
        humanInput.attachCommandTriggers(this.cmdClimb, this.cmdRaiseElevator);
    }
}
```
# robot.humanInterface.feedback

This package is used for classes related to receiving input from the human(s).

Classes which relate more to human input than anything else belong here. Some examples include:

 - `Trigger` implementations based on human input
 - Custom wrappers around human input devices
 
At minimum, this should package should always contain a `HumanInput` class -- this project includes a stub and there is
detailed documentation about it below.

## HumanInput

At minimum, there should be a `HumanInput` class which encapsulates all of the human input devices and provides an 
intuitive set of methods which hide the details of exactly which devices are being used. This is a programming 
principal known as _[information hiding](https://en.wikipedia.org/wiki/Information_hiding)_ and it makes it easier
to make changes to the control scheme without impacting the rest of the robot.

For example...

```java

class BadHumanInput {
    // ...
    GamePad getGamePad() { return gamePad; }
}

class Wrist extends Subsystem {
    // ...
    @Override
    public void execute() {
        motor.setSpeed(humanInput.getGamePad().ls().getVertical());
    }
}

class Elbow extends Subsystem {
    // ...
    @Override
    public void execute() {
        motor.setSpeed(humanInput.getGamePad().rs().getVertical());
    }
}
```

If we decide to swap left stick with right stick, we must go into each Subsystem and make changes. Of course, this
seems simple enough in this example, but imagine a doing that for a much bigger program... Now imagine doing it
between matches of a competition! Hope you didn't miss any references :-|

But by hiding the details of where those values come from, we can move things around more easily and safely...

```java

class GoodHumanInput {
    // ...
    GamePad getWristSpeed() { return gamePad.ls().getVertical(); }
    
    GamePad getElbowSpeed() { return gamePad.rs().getVertical(); }
}

class Wrist extends Subsystem {
    // ...
    @Override
    public void execute() {
        motor.setSpeed(humanInput.getWristSpeed());
    }
}

class Elbow extends Subsystem {
    // ...
    @Override
    public void execute() {
        motor.setSpeed(humanInput.getElbowSpeed());
    }
}
```

Now if we want to swap left and right stick, we just make our changes in `GoodHumanInput` and the rest of the code
just works! We could even quickly and safely start controlling the wrist using a completely different input device. With
a little bit of imagination, we have HumanInput load a key map from a configuration file -- _that would be
interesting_ -- without ever touching the rest of the Robot code.

 
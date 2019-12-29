# Subsystem

A Subsystem is a collection of parts (sensors, mechanisms, controllers) that provides a set of 
functionality to the Robot. A Robot is a collection of subsystems.

A Subsystem is instructed with Commands. Read more about commands at `/docs/COMMANDS.md`.

## Breaking the Robot into (software) Subsystems

It is possible (or even likely) that mechanical and software teams break the robot down into subsystems
differently. *This is okay!* Just be aware of it so that you can help avoid misunderstandings. This is
because the two teams use different criteria to identify subsystems.

It is also common for subsystems to become bigger or smaller as the code evolves. Having stable and 
clear goals for Robot behavior can help minimize this.

### Criteria 1: Commands control Subsystems

For the purpose of software, we think about a subsystem as part of the Robot that commands directly
instruct. For example, imagine a command that makes the robot go forward (or right, or left). It would 
not be enough to send that command to a single motor or just the left drive train. Instead we would
send that command to the entire Drive Base.

This criteria helps *reduce* the number of subsystems we have. Having many, small subsystems can make
the software overly complicated since each command will need to control several subsystems to get
anything done.

### Criteria 2: Only one Command can control the same Subsystem at the same time

When a Command takes control of a subsystem, it has exclusive control (when done correctly). This is
important to avoid "confusing" a subsystem with conflicting instructions from two different commands.
If two Commands try to use the same subsystem, one will cancel the other. If they keep fighting over
the same subsystem, they can take turns canceling each other out and nothing will happen!

Sometimes this forces smaller subsystems so that two different commands can operate at the same
time without conflicting.

For example, imagine a 2-speed Drive Base. It seems natural to include the shifter and the drive
motors in the same subsystem. However, with this configuration the Command to "change gears" and the
Command to "drive forward" could end up fighting for control over that subsystem! Breaking the Drive 
Shifter into its own subsystem solves this problem.

This criteria *increases* the number of subsystems we have. Having many, small subsystems can make
the software overly complicated since each command will need to control several subsystems to get
anything done.

## Creating a Subsystem

### Step 1: Create a class to represent the Subsystem

We create a class that extends our subsystem base class. Don't forget to document it!

```java
import org.team1251.frc.robotCore.subsystems.TigerSubsystem;

/**
 * The subsystem responsible for collecting game pieces.
 */
class Collector extends TigerSubsystem {
    
    public Collector() {

    }
}
```

### Step 2: Create parts owned by the subsystem

We use the constructor to create the parts that make up the subsystem. Usually, we do this with the
help of one or more _factories_ (see `/docs/PART_FACTORIES.md` for more about that). Our example
Collector has one motor (controlled by a talon) and one switch used to detect whether or not the 
game piece has been collected.

All parts are assigned to `private` fields so that they can be accessed throughout the class, but not
by other classes. The fields are also marked as `final` because they never get reassigned. 

Don't forget to document your fields!

```java
class Collector extends TigerSubsystem {

    /**
     * The switch that detects whether or not a game piece has been collected.
     */
    private final NormallyOpenSwitch pieceDetector;

    /**
     * The controller for the motor that spins the collection wheels.
     */
    private final TalonSRX motor;

    public Collector() {
        this.pieceDetector = Robot.sensorFactor.createGamePieceDetector();
        this.motor = Robot.controllerFactor.createCollectorMotorController();
    }
}
```

### Step 3: Pass in external objects, if any

Sometimes, the subsystem will need an object that was created somewhere else. For example, if the 
collector needed to have access to field data. It does not **own** the field data -- other subsystems
may also need it -- so it does not make sense to _create_ it. Instead, we pass it in to the constructor
and store it in a field for later use.

```java
class Collector extends TigerSubsystem {

    // ... parts fields

    /**
     * Information about the field state so we know if we are allowed to pick up pieces.
     */ 
    private final FieldData fieldData;

    public Collector(FieldData fieldData) {
        // ... create parts
      
        this.fieldData = fieldData;
    }
}
```

### Step 4: Add Behaviors and Feedback

Commands need a way to send instructions to the Subsystem. They may also need to know details
about the subsystem's state. We define these as `public` methods. (Don't forget the docs!)

>Pro Tip: Notice that Commands do not need to know a lot of the details of the collector. For example, 
the Command doesn't need to know if it is using a switch or a proximity sensor to detect the game piece. 
It also doesn't need to know how many motor are controlling the collector. This is called `encapsulation`
(or `information hiding`) and is one of the key principles of Object-Oriented Design._  

```java
class Collector extends TigerSubsystem {

    // ... fields

    // ... constructor
 
    /**
     * Set the speed of the collector.
     *
     * If piece collection is not currently allowed by the field, speed will be forced to 0 (stopped).
     *
     * @param speed - A value between -1 and 1 indicating the direction and speed of the motor. Values
     *    less than 0 will run the collector backwards and values greater than 0 will run it forwards.
     *    Use 0 to stop the collector.
     */
    public void runCollector(double speed) {
        // See if the field is allowing collection.
        if (fieldData.isCollectionAllowed()) {
            // collection allowed, go for it!
            motor.setSpeed(speed);
        } else {
            // No collection allowed, force the motor to stop.
            motor.setSpeed(0);
        }
    }
   
    /**
     * Indicates whether or not a game piece has been collected.
     *
     * @return True if piece is currently in possession, otherwise False
     */
    public boolean isPieceCollected() {
        return pieceDetector.isActive();
    }
}
```

### Step 5: Create the Subsystem instance

We have a `class` to represent our Subsystem, but we still need to create an instance (instantiate) it
before it can be used. This is done in the `robotInitCreateSubsystems()` method of our `Robot` class and
the instance is stored in a `private` field for later use.

```java
class Robot extends TigerTimedRobot {
    /**
     * Subsystem for collecting game pieces.
     */
    private Collector collector;

    @Override
    public void robotInitCreateSubsystems() {
        this.collector = new Collector(this.fieldData);
    }
}
```

And that's it! Your subsystem is ready to be used by Commands.

### But wait! Where did `this.fieldData` come from?!

Dependencies (such as FieldData in our example) must be created **before** it can be passed into
the Collector constructor. It _could_ be created in `robotInitCreateSubsystems()` but that would
be a little weird since it isn't a subsystem!

If we look at our Robot Init Lifecycle, there are two methods that are ran earlier: `robotInitPrep()`,
and `robotInitFeedbackSystem()`. Since data from the field is a type of feedback, we'll use the latter.
You can learn more in the `/docs/ROBOT_INIT.md` but let's expand the above example to put it in context.


```java
class Robot extends TigerTimedRobot {
    /**
     * Subsystem for collecting game pieces.
     */
    private Collector collector;
 
    /**
     * Information about the field, as provided by the FMS.
     */
    private FieldData fieldData;


    @Override
    public void robotInitCreateFeedbackSystems() {
        this.fieldData = new FieldData();
    }

    @Override
    public void robotInitCreateSubsystems() {
        this.collector = new Collector(this.fieldData);
    }
}
```
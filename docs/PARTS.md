# Robot "Parts"

Robots are made up of Subsystems and Subsystems are made up of `Parts`. 

Some very common parts (such as the PDP) are represented by classes in the WPILib library and others 
(like CTRE motor controllers) are represented by classes provided in vendor libraries. We even have 
some parts that are used from year-to-year represented as classes in the `robotCore.parts` package. 
Parts that are not already defined in one of these places are created as classes under the 
`robot.parts` package.

A `Part` may be a single component (such as a sensor) or it may be a logical grouping of other parts. 
We generalize parts in three categories: `controllers', 'mechanisms`, and `sensors`.

## "Wrappers"

Some part classes just contain (or "wrap") a different part. These are used to make it easier or
more logical to interact with the part. These "wrapper" classes are very common in Object Oriented
Programming. For an example, check out `robotCore.parts.sensors.NormallyOpenSwitch`.

## Controllers

These are parts that receive control signals and use them to control other components. Common examples
are `motor controllers` and `solenoids`. 

Because FRC limits what controllers are allowed on the Robot, most of these are defined by the WPI or
vendor libraries so custom controller classes are uncommon (unless they are [wrappers](#wrappers)).

## Sensors

These are parts that provide information about the Robot or the world around it.

Data from most sensors is read over digital or analog inputs, so custom sensor classes are often
[wrappers](#wrappers) around the WPI `AnalogInput` or `DigitalInput` classes. For an example, look at the
`frc.robotCore.parts.sensors.NormallyOpenSwitch` class.

Sometimes, a sensor class may combine multiple sensors that work together to provide useful information. 
For example, if the Robot uses two bumper switches to tell if it has collected a game piece, the sensor 
class could combine them into a single class that is easier to use.
```java
package org.team1251.frc.robot.parts.sensors;

class PieceDetector {
    private final NormallyOpenSwitch leftBumperSwitch;
    private final NormallyOpenSwitch rightBumperSwitch;

    // ... constructor
    
   public boolean isPieceCollected() {
      // Piece is collected if either bumper switch is active.
      return leftBumperSwitch.isActive() || rightBumperSwitch.isActive();
   }

}
``` 

## Mechanisms

Mechanisms are logical groupings of other parts to make up a functional piece of the Robot.

Mechanism classes are useful breaking complicated Subsystems down into smaller, easier to understand classes. 
For example, a very complicated Arm subsystem may have many motors and sensors that help control each motor. 
Instead of managing each motor and sensor directly in the Arm class, it may be easier to create a separate 
`mechansim` classes for the "elbow" and the "claw". Each one would then only contain the motor controllers 
and sensors it needs instead of the Arm class managing it all.

Mechanism classes are also very useful if the same mechanism shows up more than one time in the Robot. For
example, imagine our Arm from the above example as having two (or three!) elbows. In this example, every 
elbow works the same way: one motor to move and and one potentiometer to track position.

```java
package org.team1251.frc.robot.parts.sensors;

class Elbow {
    // ... fields

    // ... constructor

    public double getAngle() {
       return potentiometer.get();
    }

    public void move(double speed) {
       if (speed > 0) {
            // Don't extend past 90 degrees when moving forward!
            if (getAngle() >= 90) {
                motor.set(0);
            } else {
                motor.set(speed);
            }
       } else if (speed < 0) {
           // Don't extend past 0 degrees when moving backward!
           if (getAngle() <= 0) {
               motor.set(0);
           } else {
               motor.set(speed);
           }
       } else {
           // Not more than 0 and not less than 0, stop the arm.
           motor.set(0);
       }
    }
}
```

Now, the Arm subsystem can contain as many elbows as it needs without repeating all the details!
```java
class Arm extends TigerSubsystem {
    private final Elbow elbowOne;
    private final Elbow elbowTwo;

    // ... constructor
   
    public void moveElbowOne(double speed) {
       elbowOne.move(speed);
    }

    public void moveElbowTwo(double speed) {
       elbowTwo.move(speed);
    }
}
```

As a bonus, if two limit switches are added to all the elbows as a an extra failsafe then the Elbow 
class can be modified to include two switches instead of adding FOUR switches to the Arm subsystem. In
fact, the Arm subsystem wouldn't need to change at all!

# Part Factories


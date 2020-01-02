# RobotMap

The "Robot Map" is a term introduced by the WPI lib documentation and examples. It is a place to define global Robot
constants. WPI presents it as a single class, `RobotMap`. We present it as a package of classes, 
`org.1251.robot.robootMap`.

The most documented use of Robot Map is for defining the device channels. To make things more organized, we split
this up into 4 `enum` classes: `AnalogDevice`, `CanDevice`, `DioDevice`, and `PcmDevice`.

>Pro-tip: An `enum` is a special type of class that lists a finite set related constant values. Unlike regular 
>constant , each `enum` value is a full object and which may have fields and methods just like any other class object.
>The only difference is that all fields MUST contain constant values. The classic example is an `enum` that lists the
>days of the week as its values. 
>
>See the [Java documentation](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html) for more information.

These classes have no values to start with; the values must be added by you because each Robot is different! They may
be used from anywhere within the Robot but are most commonly used by [Part Factories](PART_FACTORIES.md).

## AnalogDevice

The `AnalogDevice` enum is used to identify the `channel` for every analog device that is connected to the robot. Take
this example from the 2019 robot which had two analog sensors for detecting the ground near the front and rear of the 
robot.

```java
package org.team1251.frc.robot.robotMap;

public enum AnalogDevice {
    IR_CLIMB_GROUND_SENSOR_FRONT(1), // Sharp IR
    IR_CLIMB_GROUND_SENSOR_REAR(0); // Sharp IR

    public final int channel;

    AnalogDevice(int channel) {
        this.channel = channel;
    }
}
```

The channel (i.e. which RoboRio Analog port the sensor is plugged into) for each sensor can then be retrieved from 
any place in the Robot code. For example, the following line would output the channel of the front ground sensor to 
the console from anywhere within the Robot code.
 ```java
System.out.println(AnalogDevice.IR_CLIMB_GROUND_SENSOR_FRONT.channel);
```

## CanDevice

The `CanDevice` enum works just like the [`AnalogDevice`](#analogdevice) enum except it presents `deviceNum` instead
of `channel`.

For example, here is a truncated version of `PcmDevice` from the 2019 Robot.

```java
package org.team1251.frc.robot.robotMap;

public enum CanDevice {
    // PDP (MUST be 0)
    PDP(0),

    // Left drive train.
    MC_DRIVE_LEFT_TOP(1), // Talon
    MC_DRIVE_LEFT_FRONT(2), // Victor
    MC_DRIVE_LEFT_REAR(3), // Victor

    public final int deviceNum;

    CanDevice(int deviceNum) {
        this.deviceNum = deviceNum;
    }
}
```

And this is how you would print the device number for the top left drive motor controller from any other section
of the Robot Code.
 ```java
System.out.println(CanDevice.MC_DRIVE_LEFT_TOP.deviceNum);
```

>Pro-tip: Notice the PDP is defined as being identified as device number `0`. According to the WPI lib documentation 
>the PDP must **always** be assigned to this CAN device number. 

## DioDevice

The `CanDevice` enum works just like the [`AnalogDevice`](#analogdevice) enum.

For example, here is a the of `DioDevice` enum from the 2019 Robot.

```java
package org.team1251.frc.robot.robotMap;

public enum DioDevice {

    //Limit Switch
    LS_FRONT_LIFT_LEG_LOWER(1), // Lever Switch
    LS_REAR_LIFT_LEG_LOWER(7); // Lever Switch

    public final int channel;

    DioDevice(int channel) {
        this.channel = channel;
    }
}
```

And this is how you would print the channel for the front lift leg's lower limit switch from any other section
of the Robot Code.
 ```java
System.out.println(CanDevice.LS_FRONT_LIFT_LEG_LOWER.channel);
```

>Pro-tip: Even though `DioDevice` and `AnalogDevice` look exactly alike we use separate enum classes instead of one
>that lists channels for both digital and analog devices because they are collections of different things. This 
>improves code organization and makes it easier to make changes and fix bugs.


## PcmDevice

The `PcmDevice` enum works just like the [`AnalogDevice`](#analogdevice) enum except it presents two fields instead of
one. Because you can have more than one Pneumatics-Control-Modules (PCMs) on the same Robot, you must be able to 
identify the module as well as the channel. These are identified with the `module` and `channel` fields, respectively 

For example, here is a truncated version of `PcmDevice` from the 2019 Robot.

```java
package org.team1251.frc.robot.robotMap;

public enum PcmDevice {

    DSOL_CLIMB_FRONT_LEG_ENGAGER_FWD(0, 6), // Double Solenoid
    DSOL_CLIMB_FRONT_LEG_ENGAGER_REV(0, 7), // Double Solenoid

    public final int channel;
    public final int module;

    PcmDevice(int channel, int module) {
        this.channel = channel;
        this.module = module;
    }
}

```

And this is how you would print the channel and module for the fwd port of the double-solenoid that engages 
(or disengages) the front climb leg.
 ```java
System.out.println(CanDevice.LS_FRONT_LIFT_LEG_LOWER.module + "->" + CanDevice.LS_FRONT_LIFT_LEG_LOWER.channel);
```

# robot.robotMap

The name of this package is derived from the `RobotMap` class which comes with the default WPI Lib robot template 
because it focuses on the same goal:

  Making it easy to manage device assignments as the robot evolves.
  
It usually has at least two classes in it:

   - `enum DeviceConnector implements DeviceConnectorInterface`
   - `class DeviceManager extends AbstractDeviceManager<DeviceConnector>`

A stub of each of these classes are provided as part of the template project. These classes work in conjunction 
with the classes within the `org.team1251.frc.robotCore.robotMap.*` package of the `robot-core` project.
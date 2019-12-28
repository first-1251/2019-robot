# Telemetry Data

Telemetry data is information intended to let Humans see near real-time information about the Robot. It may be used 
for testing, diagnostics, or operator feedback. It can even be collected and stored so that it can be inspected 
later to find out why the Robot behaved in an unexpected way.

FRC Robots already send a lot of telemetry data by default. It is used by the Driver Station software uses it to create 
its logs and graphs that provide important information during the match such as electrical system details (voltage,
amperage, brownouts, etc).

This Framework also provides a way to write custom Telemetry data. This data data is generalized into two categories:

  * `Sensors` - Direct representation of data from sensors. For example, the distance or ticks being reported 
    by an encoder.
  * `State` - Interpretation of software state and sensor data to represent the state of the Robot. For example, 
    whether or not the Robot is in possession of a game piece.
    
    
Like the built-in telemetry data, the custom data is sent using FRC Network Tables system. Sensor data and State data 
are written into the  `Sensors` and `State` Network Tables, respectively.
 
## Adding Custom Telemetry Data.
 
Any class may be used to send custom telemetry data...
    
### Step 1: Make a `Telemetry Provider`

In this example, we will make our Collector Subsystem into a Telemetry Provider but any class that has interesting
information to report may be made into one using the same steps.

To do this, we first declare that our Collector can act as Telemetry Provider by adding `implements ITelemetryProvider`
to the class definition and adding the `sendTelemetryData(...)` method.
```java
class Collector extends Subsystem implements ITelemetryProvider {
   // ...
   @Override
   public void sendTelemetryData(TelemetryTables telemetryTables) {
     
   }
}
```

Then, we get a Network Table that we can write our data to. We are going to report whether or not we have collected
a game piece, so we want to write that to the "State" table.
```java
class Collector extends Subsystem implements ITelemetryProvider {
    // ...
    @Override
    public void sendTelemetryData(TelemetryTables telemetryTables) {
       NetworkTable stateTable = telemetryTables.getStateTable();  
    }
}
```

Now, we just write our telemetry data to the Network Table
```java
class Collector extends Subsystem implements ITelemetryProvider {
    // ...
    @Override
    public void sendTelemetryData(TelemetryTables telemetryTables) {
       NetworkTable stateTable = telemetryTables.getStateTable();
       stateTable.getEntry("pieceCollected").setBoolean(this.isPieceCollected());  
    }
}
```

**That's it!** But we can make it a little bit better by writing to a sub-table that is named the same as our subsystem
```java
class Collector extends Subsystem implements ITelemetryProvider {
    // ...
    @Override
    public void sendTelemetryData(TelemetryTables telemetryTables) {
       NetworkTable stateTable = telemetryTables.getStateTable().getSubTable(this.getName());
       stateTable.getEntry("pieceCollected").setBoolean(this.isPieceCollected());  
    }
}
```

Great! Now, our Subsystem knows how to write a _true/false_ value to the `State\Collector\pieceCollected` 
Network Table. But it won't be available in our dashboard until we register it as a telemetry sender.

### Step 2: Register the `Telemetry Provider`

Earlier, we said that any class could be made into a Telemetry Provider and that's true! But we still need something
to tell it to send its telemetry data. Luckily, we can make this happen automatically by adding it to the
`Robot.telemetrySender`.

Since our Telemetry Provider is a Subsystem, let's start out the same way we do for all of our Subsystems. We'll
create the subsystem during `robotInitCreateSubsystems()` and assign it to a private field so that we can access it
everywhere within the `Robot` class.
```java
class Robot extends TigerTimedRobot {
    // ...
    private final Collector collector;

    @Override
    protected void robotInitCreateSubsystems() {
        this.collector = new Collector();
    }
}
```

Now all we have to do is add it to the `telemetrySender`. It usually makes sense to do this during the last phase
of robot initialization.
```java
class Robot extends TigerTimedRobot {
    // ...
    private final Collector collector;

    @Override
    protected void robotInitCreateSubsystems() {
        this.collector = new Collector();
    }
   
    @Override
    protected void robotInitFinalize() {
        this.telemetrySender.add(this.collector);
    }
}
``` 

And we're done! The collector's telemetry data will be automatically sent on every period ("tick") of the Robot.

_(If you are curious how it gets sent, take a peek at `TigerTimedRobot.robotPeriodic()`)_
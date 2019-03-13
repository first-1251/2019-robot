package org.team1251.frc.robotCore.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.humanInterface.feedback.ITelemetryProvider;
import org.team1251.frc.robot.robotMap.DeviceManager;

/**
 * A subsystem that does not have an internally defined default command.
 *
 * A default command can still be applied using {@link edu.wpi.first.wpilibj.command.Subsystem#setDefaultCommand(Command)}.
 * This supports the Team 1251 pattern of using dependency injection. Using this class is simply a convenience over
 * the boilerplate operation of creating an empty `initDefaultCommand()` every time.
 */
public abstract class Subsystem extends edu.wpi.first.wpilibj.command.Subsystem implements ITelemetryProvider {

    private final DeviceManager deviceManager = Robot.deviceManager;

    private final NetworkTable sensorTable;
    private final NetworkTable stateTable;

    public Subsystem() {
        super();
        sensorTable = Robot.telemetryTables.getSensorTable().getSubTable(getName());
        stateTable = Robot.telemetryTables.getStateTable().getSubTable(getName());
    }

    @Override
    protected void initDefaultCommand() { /* do nothing */ }

    protected NetworkTable getSensorTable() {
        return sensorTable;
    }

    protected NetworkTable getStateTable() {
        return stateTable;
    }

    protected DeviceManager getDeviceManager() {
        return deviceManager;
    }
}

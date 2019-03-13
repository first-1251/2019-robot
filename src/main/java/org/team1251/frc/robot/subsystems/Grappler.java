package org.team1251.frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.feedback.NormallyOpenSwitch;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Grappler extends Subsystem {

    //So we can access the Solenoids and Motors
    private final DeviceManager deviceManager = Robot.deviceManager;

    private static final DoubleSolenoid.Value EXTEND_VALUE = DoubleSolenoid.Value.kForward;
    private static final DoubleSolenoid.Value RETRACT_VALUE = DoubleSolenoid.Value.kReverse;

    private static final DoubleSolenoid.Value CLAMP_VALUE = DoubleSolenoid.Value.kForward;
    private static final DoubleSolenoid.Value UNCLAMP_VALUE = DoubleSolenoid.Value.kReverse;


    private final DoubleSolenoid extenderSolenoid;

    //Solenoid
    private final DoubleSolenoid fingerSolenoid;

    private final NormallyOpenSwitch collectionSwitch;

    public Grappler(){
        collectionSwitch = deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_PANEL_COLLECT);
        extenderSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_ARM_FWD, DeviceConnector.DSOL_PANEL_ARM_REV);
        fingerSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_GRAPPLER_FWD, DeviceConnector.DSOL_PANEL_GRAPPLER_REV);
    }

    public void extend() {
        extenderSolenoid.set(EXTEND_VALUE);
    }

    public void retract() {
        extenderSolenoid.set(RETRACT_VALUE);
    }

    public void clamp() {

        fingerSolenoid.set(CLAMP_VALUE);
    }

    public void unclamp() {
        fingerSolenoid.set(UNCLAMP_VALUE);
    }

    public boolean hasPanel() {
        return collectionSwitch.isActive();
    }

    @Override
    public void sendTelemetryData() {

        NetworkTable sensorTable = getSensorTable();
        sensorTable.getEntry("collectionSwitch").setBoolean(collectionSwitch.isActive());

        NetworkTable stateTable = getStateTable();
        stateTable.getEntry("isFingerClamped").setBoolean(fingerSolenoid.get() == CLAMP_VALUE);
        stateTable.getEntry("isExtended").setBoolean(extenderSolenoid.get() == EXTEND_VALUE);
        stateTable.getEntry("hasPanel").setBoolean(hasPanel());
    }
}

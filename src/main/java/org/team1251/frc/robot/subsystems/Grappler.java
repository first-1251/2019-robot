package org.team1251.frc.robot.subsystems;

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


    private final DoubleSolenoid armSolenoid;

    //Solenoid
    private final DoubleSolenoid fingerSolenoid;

    private final NormallyOpenSwitch collectionSwitch;


    public Grappler(){
        collectionSwitch = deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_PANEL_COLLECT);
        armSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_ARM_FWD, DeviceConnector.DSOL_PANEL_ARM_REV);
        fingerSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_GRAPPLER_FWD, DeviceConnector.DSOL_PANEL_GRAPPLER_REV);
    }

    public void extend() {
        armSolenoid.set(EXTEND_VALUE);
    }

    public void retract() {
        armSolenoid.set(RETRACT_VALUE);
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
}

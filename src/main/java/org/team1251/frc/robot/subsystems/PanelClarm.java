package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class PanelClarm extends Subsystem {

    //So we can access the Solenoids and Motors
    private final DeviceManager deviceManager = Robot.deviceManager;


    private DoubleSolenoid panelArmSolenoid;

    //Solenoid
    private DoubleSolenoid panelClawSolenoid;

    public PanelClarm(){

        panelArmSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_ARM_FWD, DeviceConnector.DSOL_PANEL_ARM_REV);
        panelClawSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_CLAW_FWD, DeviceConnector.DSOL_PANEL_CLAW_REV);

    }

    public void ExtendPanelArm (){
        panelArmSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void RetractPanelArm(){
        panelArmSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void OpenPanelClaw(){
        panelClawSolenoid.set(DoubleSolenoid.Value.kForward);
    }

}

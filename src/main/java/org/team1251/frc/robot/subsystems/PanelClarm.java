package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class PanelClarm extends Subsystem {

    //So we can access the Solenoids and Motors
    private final DeviceManager deviceManager = Robot.deviceManager;


    private DoubleSolenoid panelArmSolenoid;
    private Solenoid panelClawSolenoid;

    public PanelClarm(){

        panelArmSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_FORWARD_PANEL_ARM, DeviceConnector.DSOL_BACKWARD_PANEL_ARM);
        panelClawSolenoid = deviceManager.createSolenoid(DeviceConnector.SOL_PANEL_CLAW);

    }

    public void ExtendPanelArm (){
        panelArmSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void OpenPanelClaw(){
        panelClawSolenoid.set(true);
    }

}

package org.team1251.frc.robot.robotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import org.team1251.frc.robot.parts.sensors.GroundDetector;

public class DeviceFactory {

    private PowerDistributionPanel pdpInstance;

    private DoubleSolenoid createDoubleSolenoid(PcmDevice forwardChannel, PcmDevice backwardChannel){
        return new DoubleSolenoid(forwardChannel.module, forwardChannel.channel, backwardChannel.channel);
    }

    public GroundDetector createFrontGroundDetector() {
        return new GroundDetector(new AnalogInput(AnalogDevice.IR_CLIMB_GROUND_SENSOR_FRONT.channel));
    }

    public GroundDetector createRearGroundDetector() {
        return new GroundDetector(new AnalogInput(AnalogDevice.IR_CLIMB_GROUND_SENSOR_REAR.channel));
    }

    public PowerDistributionPanel getPDP() {
        // Special handling for the PDP. Only ever create a single instance and always return that instance.
        if (pdpInstance != null) {
            return pdpInstance;
        }

        return pdpInstance = new PowerDistributionPanel(CanDevice.PDP.deviceNum);
    }
}

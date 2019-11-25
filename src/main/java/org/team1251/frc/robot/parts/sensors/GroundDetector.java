package org.team1251.frc.robot.parts.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

public class GroundDetector {

    /**
     * Any reading above this level indicates that the ground has been detected.
     *
     * Reference: http://www.sharp-world.com/products/device/lineup/data/pdf/datasheet/gp2y0a41sk_e.pdf
     */
    private final static double GROUND_DETECTION_THRESHOLD = 1.2;

    private final AnalogInput distanceSensor;

    public GroundDetector(AnalogInput distanceSensor) {
        this.distanceSensor = distanceSensor;
    }

    public boolean isGroundDetected() {
        return distanceSensor.getAverageVoltage() >= GROUND_DETECTION_THRESHOLD;
    }

    public double getVoltage() {
        return distanceSensor.getAverageVoltage();
    }
}

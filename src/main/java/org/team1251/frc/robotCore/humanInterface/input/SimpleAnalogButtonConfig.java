package org.team1251.frc.robotCore.humanInterface.input;

public class SimpleAnalogButtonConfig implements AnalogButtonConfig {

    private final double deadZone;
    private final double pressedThreshold;

    public SimpleAnalogButtonConfig(double deadZone, double pressedThreshold) {
        this.deadZone = deadZone;
        this.pressedThreshold = pressedThreshold;
    }

    @Override
    public double getDeadZone() {
        return deadZone;
    }

    @Override
    public double getPressedThreshold() {
        return pressedThreshold;
    }
}

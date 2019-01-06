package org.team1251.frc.robotCore.humanInterface.input;

public class SimpleStickConfig implements StickConfig {

    private final double deadZone;
    private final boolean isHorizontalInverted;
    private final boolean isVerticalInverted;

    public SimpleStickConfig(double deadZone, boolean isHorizontalInverted, boolean isVerticalInverted) {
        this.deadZone = deadZone;
        this.isHorizontalInverted = isHorizontalInverted;
        this.isVerticalInverted = isVerticalInverted;
    }

    @Override
    public double getDeadZone() {
        return deadZone;
    }

    @Override
    public boolean isHorizontalInverted() {
        return isHorizontalInverted;
    }

    @Override
    public boolean isVerticalInverted() {
        return isVerticalInverted;
    }
}

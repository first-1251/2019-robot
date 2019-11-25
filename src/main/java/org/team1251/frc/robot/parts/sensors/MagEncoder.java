package org.team1251.frc.robot.parts.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class MagEncoder {

    private final static int COUNTS_PER_REVOLUTION = 4096;
    private final TalonSRX talon;
    private final double distancePerCount;

    public MagEncoder(TalonSRX talon, double distancePerRevolution, boolean isPhaseInverted) {
        this.talon = talon;
        this.distancePerCount = distancePerRevolution / COUNTS_PER_REVOLUTION;
        talon.setSensorPhase(isPhaseInverted);
    }

    public double getVelocity() {
        return talon.getSensorCollection().getQuadratureVelocity();
    }

    public int getPosition() {
        return talon.getSensorCollection().getQuadraturePosition();
    }

    public int getAbsolutePosition() {
        return talon.getSensorCollection().getPulseWidthPosition();
    }

    public double getDistance() {
        return getPosition() * distancePerCount;
    }

    public void reset() {
        talon.getSensorCollection().setQuadraturePosition(0, 2);
    }
}

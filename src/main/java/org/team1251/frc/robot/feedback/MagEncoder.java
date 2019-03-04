package org.team1251.frc.robot.feedback;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class MagEncoder {

    private final static int COUNTS_PER_REVOLUTION = 4096;
    private final TalonSRX talon;
    private final double distancePerCount;


    public MagEncoder(TalonSRX talon, double distancePerRevolution) {
        this.talon = talon;
        this.distancePerCount = distancePerRevolution / COUNTS_PER_REVOLUTION;
    }

    public MagEncoder(TalonSRX talon, double distancePerRevolution, boolean isPhaseInverted) {
        this(talon, distancePerRevolution);
        talon.setSensorPhase(isPhaseInverted);
    }

    public int getPosition() {
        return talon.getSensorCollection().getQuadraturePosition();
    }

    public double getDistance() {
        return getPosition() * distancePerCount;
    }

    public void reset() {
        talon.getSensorCollection().setQuadraturePosition(0, 2);
    }
}
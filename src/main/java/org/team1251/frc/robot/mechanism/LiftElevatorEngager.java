package org.team1251.frc.robot.mechanism;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class LiftElevatorEngager {

    private final boolean isInverted;
    private final DoubleSolenoid solenoid;

    public LiftElevatorEngager(DoubleSolenoid solenoid, boolean isInverted) {
        this.solenoid = solenoid;
        this.isInverted = isInverted;
    }

    public void setState(boolean isEngaged) {

        if (isEngaged) {
            this.solenoid.set(isInverted ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        } else {
            this.solenoid.set(isInverted ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        }

    }
}

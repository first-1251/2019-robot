package org.team1251.frc.robot.parts.mechanisms;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class MechanismFactory {
    public DriveTrain createLeftDriveTrain() {
        return new DriveTrain(DriveTrain.Identifier.LEFT);
    }

    public DriveTrain createRightDriveTrain() {
        return new DriveTrain(DriveTrain.Identifier.RIGHT);
    }

    public LiftLeg createFrontLiftLeg(TalonSRX encoderTalon) {
        return new LiftLeg(LiftLeg.Identifier.FRONT, encoderTalon);
    }

    public LiftLeg createRearLiftLeg(TalonSRX encoderTalon) {
        return new LiftLeg(LiftLeg.Identifier.REAR, encoderTalon);
    }
}

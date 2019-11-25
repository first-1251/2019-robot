package org.team1251.frc.robot.parts.mechanisms;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robot.parts.sensors.MagEncoder;
import org.team1251.frc.robot.parts.sensors.NormallyOpenSwitch;
import org.team1251.frc.robot.parts.sensors.SensorFactory;

/**
 * An individual leg of the lifter.
 */
public class LiftLeg {

    private final MagEncoder encoder;
    private final NormallyOpenSwitch lowerLimitSwitch;
    private DoubleSolenoid engagerSolenoid;
    private boolean isEngagerSolenoidInverted;

    private String telemetryLabelEncoderDistance;
    private String telemetryLabelEncoderVelocity;
    private String telemetryLabelEncoderPosition;
    private String telemetryLabelLowerLimitSwitch;

    private static final double RETRACTED_DISTANCE_THRESHOLD = .10;

    /**
     * Identifies which lift leg this instance represents (front or back)
     */
    public enum Identifier {
        FRONT, REAR
    }

    /**
     * Creates either the front or rear lift leg.
     *
     * @param identifier Identifies which lift leg this is
     * @param encoderTalon The talon to which this lift leg's encoder is attached. This is an unusual case where
     *                     the lift leg is not exclusively controlled by the same talon that its encoder is attached
     *                     to. Each of the two lift legs is monitored using an encoder attached to one of the TWO
     *                     talons controlling it, but controlled by BOTH talons.
     */
    public LiftLeg(Identifier identifier, TalonSRX encoderTalon) {
        ControllerFactory controllerFactory = Robot.controllerFactory;
        SensorFactory sensorFactory = Robot.sensorFactory;

        if (identifier == Identifier.FRONT) {
            engagerSolenoid = controllerFactory.createClimberFrontLiftLegEngagerSolenoid();
            isEngagerSolenoidInverted = true;

            lowerLimitSwitch = sensorFactory.createFrontLiftLegLowerLimitSwitch();
            encoder = sensorFactory.createFrontLiftLegEncoder(encoderTalon);
            encoder.reset();
        } else {
            engagerSolenoid = controllerFactory.createClimberRearLiftLegEngagerSolenoid();
            isEngagerSolenoidInverted = true;

            lowerLimitSwitch = sensorFactory.createRearLiftLegLowerLimitSwitch();
            encoder = sensorFactory.createRearLiftLegEncoder(encoderTalon);
            encoder.reset();
        }
    }

    private void setTelemetryLabels(Identifier identifier) {
        String prefix = identifier.name().toLowerCase() + "LiftLeg";

        telemetryLabelLowerLimitSwitch = prefix + "LowerLimitSwitch";
        telemetryLabelEncoderDistance = prefix + "EncoderDistance";
        telemetryLabelEncoderPosition = prefix + "EncoderPosition";
        telemetryLabelEncoderVelocity = prefix + "EncoderVelocity";
    }


    public void engage() {
        this.engagerSolenoid.set(isEngagerSolenoidInverted ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }

    public void disengage() {
        this.engagerSolenoid.set(isEngagerSolenoidInverted ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public boolean isEngaged() {
        return (engagerSolenoid.get() == DoubleSolenoid.Value.kForward || isEngagerSolenoidInverted);
    }

    public boolean isAtMechanicalMax() {
        return lowerLimitSwitch.isActive();
    }

    // Indicates that the leg is retracted (enough).
    public boolean isRetracted() {
        return (liftDistance() <= RETRACTED_DISTANCE_THRESHOLD);
    }

    public double liftDistance() {
        return encoder.getDistance();
    }

    public void sendTelemetryData(NetworkTable sensorTable) {

        sensorTable.getEntry(telemetryLabelEncoderDistance).setDouble(encoder.getDistance());
        sensorTable.getEntry(telemetryLabelEncoderVelocity).setDouble(encoder.getVelocity());
        sensorTable.getEntry(telemetryLabelEncoderPosition).setDouble(encoder.getPosition());

        sensorTable.getEntry(telemetryLabelLowerLimitSwitch).setBoolean(lowerLimitSwitch.isActive());


    }



}

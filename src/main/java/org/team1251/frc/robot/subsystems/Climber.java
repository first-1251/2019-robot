package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.NetworkTable;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robot.parts.mechanisms.LiftLeg;
import org.team1251.frc.robot.parts.mechanisms.MechanismFactory;
import org.team1251.frc.robot.parts.sensors.GroundDetector;
import org.team1251.frc.robot.parts.sensors.SensorFactory;
import org.team1251.frc.robotCore.humanInterface.feedback.ITelemetryProvider;
import org.team1251.frc.robotCore.humanInterface.feedback.TelemetryTables;
import org.team1251.frc.robotCore.subsystems.TigerSubsystem;

/**
 * The subsystem that is responsible for making the Robot climb.
 */
public class Climber extends TigerSubsystem implements ITelemetryProvider {

    /**
     * All possible lift targets.
     */
    public enum LiftTarget {
        HAB_LVL_2(8, false),
        HAB_LVL_3(21, false),
        TEST(4, true);

        public final double height;

        public final boolean isSlow;
        LiftTarget(double height, boolean isSlow) {
            this.height = height;

            this.isSlow = isSlow;
        }

    }

    /**
     * The motor power to use for a slow lift
     */
    private static final double SLOW_LIFT_POWER = .5;

    /**
     * The motor power to use for a normal lift
     */
    private static final double NORMAL_LIFT_POWER = 1;


    /**
     * The motor power to use for holding the current height.
     */
    private static final double SUSTAIN_POWER = .10;

    /**
     * The current height limit. Set by changing the lift target.
     */
    private double maxHeight = 0;

    private final LiftLeg frontLeg;
    private final LiftLeg rearLeg;

    private final GroundDetector frontGroundDetector;
    private final GroundDetector rearGroundDetector;

    private final TalonSRX frontLiftController;
    private final TalonSRX rearLiftController;

    private final TalonSRX driveController;

    /**
     * Creates a Climber instance.
     */
    public Climber() {
        ControllerFactory controllerFactory = Robot.controllerFactory;
        MechanismFactory mechanismFactory = Robot.mechanismFactory;
        SensorFactory sensorFactory = Robot.sensorFactory;

        driveController = controllerFactory.createLifterDriveMotorController();

        frontLiftController = controllerFactory.createFrontLifterMotorController();
        rearLiftController = controllerFactory.createRearLifterMotorController();

        frontLeg = mechanismFactory.createFrontLiftLeg(frontLiftController);
        rearLeg = mechanismFactory.createRearLiftLeg(rearLiftController);

        frontGroundDetector = sensorFactory.createFrontGroundDetector();
        rearGroundDetector = sensorFactory.createRearGroundDetector();
    }

    /**
     * Lifts to a specific height and holds there.
     *
     * @param target Which target to lift to.
     */
    public void liftTo(LiftTarget target) {
        this.maxHeight = target.height;
        this.lift(target.isSlow);
    }

    /**
     * Runs lift motors until fully lifted and then reduces motor power to sustain position.
     *
     * @param isSlowLift Whether or not to lift slowly (true = slow, false = normal)
     */
    private void lift(boolean isSlowLift) {
        // Engage both legs
        frontLeg.engage();
        rearLeg.engage();

        // Stop when we have reached the current lift target or when one of the legs reach its mechanical max.
        if (hasReachedLiftTarget() || frontLeg.isAtMechanicalMax() || rearLeg.isAtMechanicalMax()) {
            sustain();
        } else {
            setLiftPower(isSlowLift ? SLOW_LIFT_POWER : NORMAL_LIFT_POWER);
        }
    }

    /**
     * Runs lift motors enough to sustain the current lift position.
     */
    public void sustain() {
        setLiftPower(SUSTAIN_POWER);
    }

    /**
     * Relieves pressure on the engager for the front leg by slowly running lift motors backwards.
     */
    public void relieveFrontPressure() {
        // Only willing to drop 1 inch from the max height
        if (frontLeg.liftDistance() > (maxHeight - 1)) {
            /* NOTE: This isn't what was originally intended, but it works! Because this is only ever called after
             *   the lift height has been obtained, both motors are running at sustain power (+.10) before this
             *   logic runs. ONE of the motors is set to -.25 power creating competition between the two motors.
             */
            frontLiftController.set(ControlMode.PercentOutput, -.25);
        }
    }

    /**
     * Starts the climber's drive motor.
     */
    public void startDrive() {
        driveController.set(ControlMode.PercentOutput, 1);
    }

    /**
     * Stops the climber's driver motor.
     */
    public void stopDrive() {
        driveController.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Retracts the front lift leg if it is safe to do so without causing a large impact with the ground.
     */
    public void retractFrontLeg() {
        // Only retract if the front of the robot is on or near solid ground. Use redundant sensors to detect this
        // state: lift distance and ground sensor
        if (frontGroundDetector.isGroundDetected() || frontLeg.liftDistance() < 2.5) {
            frontLeg.disengage();
        }
    }

    /**
     * Retracts the rear lift leg if it is safe to do so without causing a large impact with the ground.
     */
    public void retractRear() {
        // Only retract if the rear of the robot is on or near solid ground. Use redundant sensors to detect this
        // state: lift distance and ground sensor
        if (rearGroundDetector.isGroundDetected() || rearLeg.liftDistance() < 2.5) {
            rearLeg.disengage();
        }
    }

    /**
     * Indicates whether or not the front leg is fully retracted (within a reasonable margin of error).
     *
     * @return Whether or not the front leg is fully retracted within a reasonable margin of error (true = retracted)
     */
    public boolean isFrontLegRetracted() {
        return frontLeg.isRetracted();
    }

    /**
     * Indicates whether or not the rear leg is fully retracted (within a reasonable margin of error).
     *
     * @return Whether or not the rear leg is fully retracted within a reasonable margin of error (true = retracted)
     */
    public boolean isRearLegRetracted() {
        return rearLeg.isRetracted();
    }

    /**
     * Kills the lift motors.
     */
    public void killLiftMotors() {
        setLiftPower(0);
    }

    /**
     * Indicates whether or not the climber has reached the lift target.
     *
     * @return Whether or not the climber has reached the lift target.
     */
    public boolean hasReachedLiftTarget() {
        // if either front or rear is at the maximum height, consider the whole bot as lifted. If legs were to become
        // unsynchronized (one higher than the other), this makes sure that there isn't an accidental overrun.
        return frontLeg.liftDistance() >= maxHeight || frontLeg.liftDistance() >= maxHeight;
    }

    /**
     * Indicates whether the rear of the robot is on solid ground.
     *
     * @return Whether the rear of the robot is on solid ground. (true = yes)
     */
    public boolean isRearOnSolidGround() {
        return rearGroundDetector.isGroundDetected();
    }

    /**
     * Indicates whether the front of the robot is on solid ground.
     *
     * @return Whether the front of the robot is on solid ground. (true = yes)
     */
    public boolean isFrontOnSolidGround() {
        return frontGroundDetector.isGroundDetected();
    }

    /**
     * Explicitly sets the front leg engager state without regard for safeties. This SHOULD NOT be used outside of
     * mechanism testing.
     *
     * @param isEngaged Which state to set the engager to (true = engaged, false = disengaged)
     */
    public void testFrontLegEngager(boolean isEngaged) {
        if (isEngaged) {
            frontLeg.engage();
        } else {
            frontLeg.disengage();
        }
    }

    /**
     * Explicitly sets the rear leg engager state without regard for safeties. This SHOULD NOT be used outside of
     * mechanism testing.
     *
     * @param isEngaged Which state to set the engager to (true = engaged, false = disengaged)
     */
    public void testRearLegEngager(boolean isEngaged) {
        if (isEngaged) {
            rearLeg.engage();
        } else {
            rearLeg.disengage();
        }
    }

    /**
     * Internal helper to set the same output power to both lift motors.
     *
     * @param power A value between -1 and 1 to use as the motor power.
     */
    private void setLiftPower(double power) {
        frontLiftController.set(ControlMode.PercentOutput, power);
        rearLiftController.set(ControlMode.PercentOutput, power);
    }


    @Override
    public void sendTelemetryData(TelemetryTables telemetryTables) {
        // Sensor Data
        NetworkTable sensorTable = telemetryTables.getSensorTable().getSubTable(getName());
        sensorTable.getEntry("rearGroundDetectState").setBoolean(rearGroundDetector.isGroundDetected());
        sensorTable.getEntry("rearGroundDetectVoltage").setNumber(rearGroundDetector.getVoltage());

        // Subsystem state data
        NetworkTable stateTable = telemetryTables.getStateTable().getSubTable(getName());
        stateTable.getEntry("hasReachedLiftTarget").setBoolean(hasReachedLiftTarget());
        stateTable.getEntry("liftTargetHeight").setDouble(maxHeight);
        stateTable.getEntry("isFrontRetracted").setBoolean(isFrontLegRetracted());
        stateTable.getEntry("isFrontOnSolidGround").setBoolean(isFrontOnSolidGround());
        stateTable.getEntry("isRearRetracted").setBoolean(isRearLegRetracted());
        stateTable.getEntry("isRearOnSolidGround").setBoolean(isRearOnSolidGround());

        // Indirect telemetry data.
        frontLeg.sendTelemetryData(sensorTable);
        rearLeg.sendTelemetryData(sensorTable);
    }
}

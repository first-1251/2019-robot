package org.team1251.frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import org.team1251.frc.robot.DrivePower;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.mechanisms.DriveTrain;
import org.team1251.frc.robot.parts.mechanisms.MechanismFactory;
import org.team1251.frc.robotCore.humanInterface.output.ITelemetryProvider;
import org.team1251.frc.robotCore.humanInterface.output.TelemetryTables;
import org.team1251.frc.robotCore.subsystems.TigerSubsystem;

/**
 * The subsystem which moves the robot around the field.
 */
public class DriveBase extends TigerSubsystem implements ITelemetryProvider {

    /**
     * An enumeration of all motors within the drive train.
     *
     * Each motor is labeled by its physical location on the robot.
     */
    public enum Motor {
        LEFT_TOP, LEFT_FRONT, LEFT_REAR,
        RIGHT_TOP, RIGHT_FRONT, RIGHT_REAR
    }

    /**
     * The mechanism that drives the left side of the robot.
     */
    private final DriveTrain leftTrain;

    /**
     * The mechanism that drives the right side of the robot.
     */
    private final DriveTrain rightTrain;

    @Override
    public void sendTelemetryData(TelemetryTables telemetryTables) {
        NetworkTable sensorTable = telemetryTables.getSensorTable().getSubTable(getName());
        sensorTable.getEntry("leftTrainVelocity").setDouble(rightTrain.getVelocity());
        sensorTable.getEntry("rightTrainVelocity").setDouble(leftTrain.getVelocity());
    }

    /**
     * Create a new drive base instance.
     */
    public DriveBase() {
        MechanismFactory mechansimFactory = Robot.mechanismFactory;
        leftTrain = mechansimFactory.createLeftDriveTrain();
        rightTrain = mechansimFactory.createRightDriveTrain();

    }


    /**
     * Starts or stops a motor. When motor is running, it runs at 50% forward power.
     *
     * If testing for a different motor is already in progress, it will be stopped before starting the test for
     * this motor.
     *
     * @param motor The motor to test or `null` to test no motor.
     */
    public void testMotor(Motor motor) {

        if (motor == null) {
            rightTrain.set(0);
            leftTrain.set(0);
            return;
        }

        double motorTestSpeed = .5;
        switch (motor) {
            case LEFT_TOP:
                rightTrain.set(0);
                leftTrain.setTop(motorTestSpeed);
                break;
            case LEFT_FRONT:
                rightTrain.set(0);
                leftTrain.setFront(motorTestSpeed);
                break;
            case LEFT_REAR:
                rightTrain.set(0);
                leftTrain.setRear(motorTestSpeed);
                break;
            case RIGHT_TOP:
                rightTrain.setTop(motorTestSpeed);
                leftTrain.set(0);
                break;
            case RIGHT_FRONT:
                rightTrain.setFront(motorTestSpeed);
                leftTrain.set(0);
                break;
            case RIGHT_REAR:
                rightTrain.setRear(motorTestSpeed);
                leftTrain.set(0);
                break;
        }
    }

    /**
     * Drive straight forward or backwards.
     *
     * @param power The power at which to drive. Negative drives backwards.
     */
    public void drive(double power) {
        leftTrain.set(power);
        rightTrain.set(power);
    }

    /**
     * Drive by applying power to the left and right drive train.
     *
     * @param power The power to apply to each train.
     */
    public void drive(DrivePower power) {
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }

    public double getLeftVelocity() {
        return leftTrain.getVelocity();
    }

    public double getRightVelocity() {
        return rightTrain.getVelocity();
    }
}

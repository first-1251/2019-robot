package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.feedback.MagEncoder;
import org.team1251.frc.robot.feedback.NormallyOpenSwitch;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class ManipulatorElevator extends Subsystem implements ManipulatorElevatorState {
    /**
     * PRE MESSAGE TO ALL THOSE READING
     * 1996 NISSAN 240SX. The car for the guy that says. My BMW is too reliable.
     * PRAISE Some Nissan Dude || (INSERT MORENO VIN HERE)
     **/

    public enum SetPoint {
        
        HOME(0), // elevator full down
        HUMAN_STATION_PANEL(0),
        HUMAN_STATION_CARGO(0),
        SHIP_CARGO(0),
        SHIP_PANEL(0),
        ROCKET_LVL1_CARGO(0),
        ROCKET_LVL2_CARGO(0),
        ROCKET_LVL3_CARGO(0),
        ROCKET_LVL1_PANEL(0),
        ROCKET_LVL2_PANEL(0),
        ROCKET_LVL3_PANEL(0);

        final double height;
        final int position;

        SetPoint(double height) {
            this.height = height;
            this.position = (int) Math.round(((height / TRAVEL_PER_ENCODER_REV) * 4096));
        }
    }

    //Just In case Motors are Inverted
    private static final boolean isMotorInverted = false;

    private static final double HOLDING_POWER = .25; // amount of power it takes to hold the elevator in place.
    private static final double TRAVEL_PER_ENCODER_REV = 1;

    private static final int CRUISE_VELOCITY = 10; // counts per 100ms TODO: Adjust to reality.
    private static final int ACCELERATION = 10; // counts per 100ms TODO: Adjust to reality.

    private static final int POSITION_TOLERANCE = 100; // How close to target position is "close enough"


    private final DeviceManager deviceManager = Robot.deviceManager;
    private final NormallyOpenSwitch limitSwitchLower;
    private final NormallyOpenSwitch limitSwitchUpper;
    private final MagEncoder encoder;

    //Speed Controller Initialization
    private WPI_TalonSRX motorController;


    public ManipulatorElevator() {

        motorController = deviceManager.createTalonSRX(DeviceConnector.MC_MANIPULATOR_ELEVATOR);
        motorController.configFactoryDefault(5);
        motorController.setInverted(isMotorInverted);
        motorController.setNeutralMode(NeutralMode.Coast);
        motorController.configMotionCruiseVelocity(CRUISE_VELOCITY);
        motorController.configMotionAcceleration(ACCELERATION);

        motorController.selectProfileSlot(0, 0);
        motorController.config_kF(0, 0); // feed-forward TODO: Calculate for elevator.
        motorController.config_kP(0, .2); // proportional gain
        motorController.config_kI(0, 0); // integral gain
        motorController.config_kD(0, 0); // derivative gain

        // Get pid, quadrature and motion magic data faster to prevent stale data.
        motorController.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2); // default 20
        motorController.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Robot.TICK_PERIOD_MS / 2); // default >100
        motorController.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, Robot.TICK_PERIOD_MS / 2); // default >100

        limitSwitchUpper = deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_MANIPULATER_ELEVATOR_TOP);
        limitSwitchLower = deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_MANIPULATER_ELEVATOR_BOTTOM);

        // Assume the elevator starts down. This zeros it even if the limit switch has failed.
        encoder = new MagEncoder(motorController, TRAVEL_PER_ENCODER_REV);
        encoder.reset();
    }

    public void neutral() {
        motorController.set(0);
    }

    public void up(double speed) {
        if (limitSwitchUpper.isActive()) {
            motorController.set(0);
        } else {
            motorController.set(Math.max(speed, 0)); // ignore received values below 0
        }
    }

    public void down(double speed) {
        if (limitSwitchLower.isActive()) {
            motorController.set(0);
        } else {
            motorController.set(-Math.max(speed, 0)); // ignore values below 0, send negative value
        }
    }

    public void moveTo(SetPoint setPoint) {

        int currentPosition = encoder.getPosition();
        if (motorController.getClosedLoopError() <= POSITION_TOLERANCE) {
            // Apply holding power so long as we are within tolerance. If the holding power allows us to shift
            // out of tolerance, we'll go back to closed-loop operation.
            motorController.set(HOLDING_POWER);
        } else if (currentPosition < encoder.getPosition() && limitSwitchLower.isActive()) {
            // Prevent moving down when the lower limit switch is active.
            // Just kill the motor for mechanical safety.
            motorController.set(0);
        } else if (currentPosition > encoder.getPosition() && limitSwitchLower.isActive()) {
            // Prevent moving up when the upper limit switch is active.
            // Kill the motor for mechanical safety.
            motorController.set(0);
        } else {
            // Let the controller do the work.
            motorController.set(ControlMode.MotionMagic, setPoint.position);
        }
    }

    @Override
    public boolean isAtBottom() {
        return limitSwitchLower.isActive() || encoder.getDistance() < 1;
    }

    /**
     * When the run method of the scheduler is called this method will be called.
     */
    @Override
    public void periodic() {
        if (limitSwitchLower.isActive()) {
            encoder.reset();
        }
    }
}



package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.DrivePower;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.feedback.Gyro;
import org.team1251.frc.robot.feedback.MagEncoder;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

/**
 * The subsystem which moves the robot around the field.
 */
public class DriveBase extends Subsystem {

    /**
     * Local reference to the global device manager.
     */
    private final DeviceManager deviceManager = Robot.deviceManager;

    /**
     * The speed controller which leads all others on the left drive train.
     */
    private final SpeedController leftTrain;

    /**
     * The speed controller which leads all others on the right drive train.
     */
    private final SpeedController rightTrain;

    /**
     * The motor controller that controls the top motor on the left side of the robot.
     *
     * ```
     *     ---         F
     *    | x |        R
     *     ---         O
     *  ---   ---      N
     * |   | |   |     T
     *  ---   ---
     * ```
     */
    private final WPI_TalonSRX leftTopMotorController;


    /**
     * The motor controller that controls the bottom, front motor on the left side of the robot.
     *
     * ```
     *     ---         F
     *    |   |        R
     *     ---         O
     *  ---   ---      N
     * |   | | x |     T
     *  ---   ---
     * ```
     */
    private final WPI_VictorSPX leftBottomFrontMotorController;

    /**
     * The motor controller that controls the bottom, rear motor on the left side of the robot.
     *
     * ```
     * ```
     *     ---         F
     *    |   |        R
     *     ---         O
     *  ---   ---      N
     * | x | |   |     T
     *  ---   ---
     * ```
     * ```
     */
    private final WPI_VictorSPX leftBottomRearMotorController;

    /**
     * The motor controller that controls the top motor on the right side of the robot.
     *
     * ```
     *     ---         F
     *    | x |        R
     *     ---         O
     *  ---   ---      N
     * |   | |   |     T
     *  ---   ---
     * ```
     */
    private final WPI_TalonSRX rightTopMotorController;


    /**
     * The motor controller that controls the bottom, front motor on the right side of the robot.
     *
     * ```
     *     ---         F
     *    |   |        R
     *     ---         O
     *  ---   ---      N
     * |   | | x |     T
     *  ---   ---
     * ```
     */
    private final WPI_VictorSPX rightBottomFrontMotorController;

    /**
     * The motor controller that controls the bottom, rear motor on the right side of the robot.
     *
     * ```
     * ```
     *     ---         F
     *    |   |        R
     *     ---         O
     *  ---   ---      N
     * | x | |   |     T
     *  ---   ---
     * ```
     * ```
     */
    private final WPI_VictorSPX rightBottomRearMotorController;

    private final MagEncoder leftEncoder;
    private final MagEncoder rightEncoder;
    private final Gyro gyro;


    /**
     * The motor which is currently in testing or `null` if no motor is in testing.
     */
    private Motor motorInTesting = null;

    /**
     * Whether or not follower mode is active.
     *
     * When follower mode is active (`true`), all motors on the left or right side of the drive base can be controlled
     * via the {@link #leftTrain} and {@link #rightTrain} fields, respectively. IMPORTANT: Directly controlling an
     * individual motor controller which is in "follower" mode will take it out of follower mode!!
     *
     * When follower mode is inactive (`false`), each motor controller can be controlled separately. This should only
     * be used when testing motors.
     */
    private boolean controllerFollowMode = false;

    @Override
    public void sendTelemetryData() {
        NetworkTable sensorTable = getSensorTable();
        sensorTable.getEntry("leftEncoderVelocity").setDouble(leftEncoder.getVelocity());
        sensorTable.getEntry("rightEncoderVelocity").setDouble(rightEncoder.getVelocity());

        if (gyro != null) {
            sensorTable.getEntry("gyroHeading").setDouble(gyro.getHeading());
            sensorTable.getEntry("gyroIsReady").setBoolean(gyro.isReady());
        }

        NetworkTable stateTable = getStateTable();
        if (gyro != null) {
            stateTable.getEntry("heading").setDouble(gyro.getHeading());
            stateTable.getEntry("probableTarget").setString("TBD"); // TODO: Find most probable target based on angle.
        }
    }

    /**
     * An enumeration of all motors within the drive train.
     *
     * Each motor is labeled by its physical location on the robot.
     */
    public enum Motor {
        LEFT_TOP, LEFT_BOTTOM_FRONT, LEFT_BOTTOM_REAR,
        RIGHT_TOP, RIGHT_BOTTOM_FRONT, RIGHT_BOTTOM_REAR
    }

    /**
     * Create a new drive base instance.
     */
    public DriveBase(Gyro gyro) {
        this.gyro = gyro;

        leftTrain = leftTopMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_DRIVE_LEFT_TOP);
        configureTalon(leftTopMotorController, false);

        leftBottomFrontMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_LEFT_BOTTOM_FRONT);
        configureVictor(leftBottomFrontMotorController, false);

        leftBottomRearMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_LEFT_BOTTOM_REAR);
        configureVictor(leftBottomRearMotorController, false);

        rightTrain = rightTopMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_DRIVE_RIGHT_TOP);
        configureTalon(rightTopMotorController, true);

        rightBottomFrontMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_RIGHT_BOTTOM_FRONT);
        configureVictor(rightBottomFrontMotorController, true);

        rightBottomRearMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_RIGHT_BOTTOM_REAR);
        configureVictor(rightBottomRearMotorController, true);

        leftEncoder = new MagEncoder(leftTopMotorController, 1);
        rightEncoder = new MagEncoder(rightTopMotorController, 1);

        // Turn on following by default.
        setControllerFollowMode(true);
    }

    /**
     * Applies standard configuration to a phoenix motor controller.
     *
     * @param controller The phoenix motor controller to configure.
     * @param isInverted Set to `true` to adjust for reversed polarity on the corresponding motor.
     */
    private void configurePhoenixController(BaseMotorController controller, boolean isInverted) {
        controller.configFactoryDefault(20);
        controller.setInverted(isInverted);
        controller.setNeutralMode(NeutralMode.Coast);
        // TODO: set dead-band -- at what power is it just a waste of energy to even try?
    }

    private void configureVictor(VictorSPX controller, boolean isInverted) {
        // Apply common configuration
        this.configurePhoenixController(controller, isInverted);

        // Our victors are used as a follower (except when testing), we can slow down the update frames for a few
        // things to conserve CAN bandwidth and help offset areas were we increase update rate. Increasing these rates
        // for followers is given as a tip in the docs:
        //     https://phoenix-documentation.readthedocs.io/en/latest/ch18_CommonAPI.html?highlight=setStatusFramePeriod
        controller.setStatusFramePeriod(StatusFrame.Status_1_General, 100);
        controller.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 100);
    }

    /**
     * Applies motor controller configuration that only applies to Talons.
     *
     * The standard phoenix configuration is automatically applied to all talons by this method.
     *
     * @param controller The talonSRX motor controller to configure.
     * @param isInverted Set to `true` to adjust for reversed polarity on the corresponding motor.
     */
    private void configureTalon(TalonSRX controller, boolean isInverted) {
        // Apply common configuration
        this.configurePhoenixController(controller, isInverted);

        // Apply talon-specific configuration.
        // TODO: Set Current limiting and other cool talon-only things.
        controller.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2 ); // default 20
    }

    /**
     * Enables or disables motor controller following.
     *
     * @param isEnabled A boolean indicating whether or not to enable following mode. Setting follow mode to its
     *                  current state does nothing.
     */
    private void setControllerFollowMode(boolean isEnabled) {
        // See if there is a change
        if (isEnabled == controllerFollowMode) {
            // No change... bail early.
            return;
        }
        controllerFollowMode = isEnabled;

        // See if we are turning following on or off.
        if (isEnabled) {
            // Set up following.
            leftBottomRearMotorController.follow(leftTopMotorController);
            leftBottomFrontMotorController.follow(leftTopMotorController);

            rightBottomRearMotorController.follow(rightTopMotorController);
            rightBottomFrontMotorController.follow(rightTopMotorController);

        } else {
            // Turn off following by explicitly setting speed on following controllers
            leftBottomRearMotorController.set(ControlMode.PercentOutput, 0);
            leftBottomFrontMotorController.set(ControlMode.PercentOutput, 0);

            rightBottomRearMotorController.set(ControlMode.PercentOutput, 0);
            rightBottomFrontMotorController.set(ControlMode.PercentOutput, 0);
        }
    }

    /**
     * Provides the motor controller which is controlling the given motor.
     */
    private IMotorController deriveController(Motor motor) {

        IMotorController controller;
        switch (motor) {
            case LEFT_TOP:
                controller = leftTopMotorController;
                break;

            case LEFT_BOTTOM_FRONT:
                controller = leftBottomFrontMotorController;
                break;

            case LEFT_BOTTOM_REAR:
                controller = leftBottomRearMotorController;
                break;

            case RIGHT_TOP:
                controller = rightTopMotorController;
                break;

            case RIGHT_BOTTOM_FRONT:
                controller = rightBottomFrontMotorController;
                break;

            case RIGHT_BOTTOM_REAR:
                controller = rightBottomRearMotorController;
                break;

            default:
                throw new RuntimeException("No controller associated with motor: " + motor);
        }

        return controller;
    }

    /**
     * Stops the motor which is currently being tested.
     */
    private void stopMotorTest() {
        // Nothing to stop
        if (motorInTesting == null) {
            return;
        }

        // Figure out which controller to use and stop the motor.
        deriveController(motorInTesting).set(ControlMode.PercentOutput, 0);
        System.out.println("Stopping motor test on: " + motorInTesting.name());
        motorInTesting = null;
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

        // See if we are just ending the motor test
        if (motor == null) {
            stopMotorTest(); // Stop existing motor, if it is running.
            setControllerFollowMode(true); // Go back to follow mode.
            return;
        }

        // Explicitly turn off following mode so that motors can be controlled individually.
        setControllerFollowMode(false);

        // Do nothing, if this motor is already being tested.
        if (motor == motorInTesting) {
            return;
        }

        // Stop whatever motor was previously being tested and mark this one as the one currently being tested.
        stopMotorTest();
        motorInTesting = motor;

        // Figure out which controller to use and run the motor at 50% forward power.
        deriveController(motor).set(ControlMode.PercentOutput, .5);
        System.out.println("Starting motor test on: " + motor.name());
    }

    /**
     * Drive straight forward or backwards.
     *
     * @param power The power at which to drive. Negative drives backwards.
     */
    public void drive(double power) {
        setControllerFollowMode(true); // Force following mode.
        leftTrain.set(power);
        rightTrain.set(power);
    }

    /**
     * Drive by applying power to the left and right drive train.
     *
     * @param power The power to apply to each train.
     */
    public void drive(DrivePower power) {
        setControllerFollowMode(true); // Force following mode.
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }

    public double getLeftVelocity() {
        return leftEncoder.getVelocity();
    }

    public double getRightVelocity() {
        return rightEncoder.getVelocity();
    }
}

package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.DrivePower;
import org.team1251.frc.robot.Robot;
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
     * The motor controller that controls the bottom motor on the left side of the robot.
     *
     * ```
     *  ---   ---
     * |   | |   |
     *  ---   ---
     *     ---
     *    | x |
     *     ---
     * ```
     */
    private final WPI_TalonSRX leftBottomMotorController;


    /**
     * The motor controller that controls the top, left motor on the left side of the robot.
     *
     * ```
     *  ---   ---
     * | x | |   |
     *  ---   ---
     *     ---
     *    |   |
     *     ---
     * ```
     */
    private final WPI_VictorSPX leftTopFrontMotorController;

    /**
     * The motor controller that controls the top, rear motor on the left side of the robot.
     *
     * ```
     *  ---   ---
     * |   | | x |
     *  ---   ---
     *     ---
     *    |   |
     *     ---
     * ```
     */
    private final WPI_VictorSPX leftTopRearMotorController;

    /**
     * The motor controller that controls the bottom motor on the right side of the robot.
     *
     * ```
     *  ---   ---
     * |   | |   |
     *  ---   ---
     *     ---
     *    | x |
     *     ---
     * ```
     */
    private final WPI_TalonSRX rightBottomMotorController;


    /**
     * The motor controller that controls the top, right motor on the right side of the robot.
     *
     * ```
     *  ---   ---
     * | x | |   |
     *  ---   ---
     *     ---
     *    |   |
     *     ---
     * ```
     */
    private final WPI_VictorSPX rightTopFrontMotorController;

    /**
     * The motor controller that controls the top, rear motor on the right side of the robot.
     *
     * ```
     *  ---   ---
     * |   | | x |
     *  ---   ---
     *     ---
     *    |   |
     *     ---
     * ```
     */
    private final WPI_VictorSPX rightTopRearMotorController;

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

    /**
     * An enumeration of all motors within the drive train.
     *
     * Each motor is labeled by its physical location on the robot.
     */
    public enum Motor {
        LEFT_BOTTOM, LEFT_TOP_FRONT, LEFT_TOP_REAR,
        RIGHT_BOTTOM, RIGHT_TOP_FRONT, RIGHT_TOP_REAR
    }

    /**
     * Create a new drive base instance.
     */
    public DriveBase() {

        leftTrain = leftBottomMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_DRIVE_LEFT_BOTTOM);
        configureTalon(leftBottomMotorController, false);

        leftTopFrontMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_LEFT_TOP_FRONT);
        configurePhoenixController(leftTopFrontMotorController, false);

        leftTopRearMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_LEFT_TOP_REAR);
        configurePhoenixController(leftTopRearMotorController, false);

        rightTrain = rightBottomMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_DRIVE_RIGHT_BOTTOM);
        configureTalon(rightBottomMotorController, false);

        rightTopFrontMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_RIGHT_TOP_FRONT);
        configurePhoenixController(rightTopFrontMotorController, false);

        rightTopRearMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_DRIVE_RIGHT_TOP_REAR);
        configurePhoenixController(rightTopRearMotorController, false);

        // Turn on following by default.
        setControllerFollowMode(true);
    }

    /**
     * Applies standard configuration to a phoenix motor controller.
     *
     * @param controller The phoenix motor controller to configure.
     * @param isInverted Set to `true` to adjust for reversed polarity on the corresponding motor.
     */
    private void configurePhoenixController(IMotorController controller, boolean isInverted) {
        controller.setInverted(isInverted);
        controller.setNeutralMode(NeutralMode.Coast);
        // TODO: set dead-band -- at what power is it just a waste of energy to even try?
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
            leftTopRearMotorController.follow(leftBottomMotorController);
            leftTopFrontMotorController.follow(leftBottomMotorController);

            rightTopRearMotorController.follow(rightBottomMotorController);
            rightTopFrontMotorController.follow(rightBottomMotorController);

        } else {
            // Turn off following by explicitly setting speed on following controllers
            leftTopRearMotorController.set(ControlMode.PercentOutput, 0);
            leftTopFrontMotorController.set(ControlMode.PercentOutput, 0);

            rightTopRearMotorController.set(ControlMode.PercentOutput, 0);
            rightTopFrontMotorController.set(ControlMode.PercentOutput, 0);
        }
    }

    /**
     * Provides the motor controller which is controlling the given motor.
     */
    private IMotorController deriveController(Motor motor) {

        IMotorController controller;
        switch (motor) {
            case LEFT_BOTTOM:
                controller = leftBottomMotorController;
                break;

            case LEFT_TOP_FRONT:
                controller = leftTopFrontMotorController;
                break;

            case LEFT_TOP_REAR:
                controller = leftTopRearMotorController;
                break;

            case RIGHT_BOTTOM:
                controller = rightBottomMotorController;
                break;

            case RIGHT_TOP_FRONT:
                controller = rightTopFrontMotorController;
                break;

            case RIGHT_TOP_REAR:
                controller = rightTopRearMotorController;
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
     * Drive by applying power to the left and right drive train.
     *
     * @param power The power to apply to each train.
     */
    public void drive(DrivePower power) {
        setControllerFollowMode(true); // Force following mode.
        leftTrain.set(power.getLeft());
        rightTrain.set(power.getRight());
    }
}

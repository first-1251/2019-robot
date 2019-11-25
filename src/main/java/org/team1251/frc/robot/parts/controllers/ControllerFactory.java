package org.team1251.frc.robot.parts.controllers;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.CanDevice;
import org.team1251.frc.robot.robotMap.PcmDevice;

/**
 * Factory for control devices such as motor controllers and solenoids.
 *
 * These are devices that receive signals and interpret them to control an actuator.
 */
public class ControllerFactory {
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
    public WPI_TalonSRX createLeftTopDriveTrainController() {
        WPI_TalonSRX controller = new WPI_TalonSRX(CanDevice.MC_DRIVE_LEFT_TOP.deviceNum);
        configureLeadDriveTrainController(controller, false);
        return controller;
    }

    /**
     * The motor controller that controls the front motor on the left side of the robot.
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
    public WPI_VictorSPX createLeftFrontDriveTrainController() {
        WPI_VictorSPX controller = new WPI_VictorSPX(CanDevice.MC_DRIVE_LEFT_FRONT.deviceNum);
        configureFollowerDriveTrainController(controller, false);
        return controller;
    }

    /**
     * The motor controller that controls the rear motor on the left side of the robot.
     *
     * ```
     *     ---         F
     *    |   |        R
     *     ---         O
     *  ---   ---      N
     * | x | |   |     T
     *  ---   ---
     * ```
     */
    public WPI_VictorSPX createLeftRearDriveTrainController() {
        WPI_VictorSPX controller = new WPI_VictorSPX(CanDevice.MC_DRIVE_LEFT_REAR.deviceNum);
        configureFollowerDriveTrainController(controller, false);
        return controller;
    }
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
    public WPI_TalonSRX createRightTopDriveTrainController() {
        WPI_TalonSRX controller = new WPI_TalonSRX(CanDevice.MC_DRIVE_RIGHT_TOP.deviceNum);
        configureLeadDriveTrainController(controller, true);
        return controller;
    }

    /**
     * The motor controller that controls the front motor on the right side of the robot.
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
    public WPI_VictorSPX createRightFrontDriveTrainController() {
        WPI_VictorSPX controller = new WPI_VictorSPX(CanDevice.MC_DRIVE_RIGHT_FRONT.deviceNum);
        configureFollowerDriveTrainController(controller, true);
        return controller;
    }

    /**
     * The motor controller that controls the rear motor on the right side of the robot.
     *
     * ```
     *     ---         F
     *    |   |        R
     *     ---         O
     *  ---   ---      N
     * | x | |   |     T
     *  ---   ---
     * ```
     */
    public WPI_VictorSPX createRightRearDriveTrainController() {
        WPI_VictorSPX controller = new WPI_VictorSPX(CanDevice.MC_DRIVE_RIGHT_REAR.deviceNum);
        configureFollowerDriveTrainController(controller, true);
        return controller;
    }

    /**
     * Applies motor controller configuration that only applies to the lead controllers (they are talons)s.
     *
     * The standard phoenix configuration is automatically applied to all talons by this method.
     *
     * @param controller The talonSRX motor controller to configure.
     * @param isInverted Set to `true` to adjust for reversed polarity on the corresponding motor.
     */
    private void configureLeadDriveTrainController(TalonSRX controller, boolean isInverted) {
        // Apply common configuration
        this.configureDriveTrainController(controller, isInverted);

        // Set how often we want encoder updates sent over CAN. Send the interval to half the robot tick.
        // This means our info shouldn't more than half a tick old (excluding sensor-read lag).
        controller.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2 ); // default 20

        // Set current limiting
        controller.configPeakCurrentLimit(30, 20);
        controller.configPeakCurrentDuration(150, 20);
        controller.configContinuousCurrentLimit(30, 20);
        controller.enableCurrentLimit(true);

    }

    /**
     * Applies configuration that is common to ALL drive train controllers.
     *
     * @param controller The controller to configure.
     * @param isInverted Set to `true` to adjust for reversed polarity on the corresponding motor.
     */
    private void configureDriveTrainController(BaseMotorController controller, boolean isInverted) {
        controller.configFactoryDefault(20);
        controller.setInverted(isInverted);
        controller.setNeutralMode(NeutralMode.Coast);
        // TODO: set dead-band -- at what power is it just a waste of energy to even try?
    }

    /**
     * Applies configuration for drive train controllers that are following the lead.
     *
     * @param controller The controller to be configured
     * @param isInverted Whether or not the polarity is inverted on this motor
     */
    private void configureFollowerDriveTrainController(VictorSPX controller, boolean isInverted) {
        // Apply common configuration
        this.configureDriveTrainController(controller, isInverted);

        // Our victors are used as a follower (except when testing), we can slow down the update frames for a few
        // things to conserve CAN bandwidth and help offset areas were we increase update rate. Increasing these rates
        // for followers is given as a tip in the docs:
        //     https://phoenix-documentation.readthedocs.io/en/latest/ch18_CommonAPI.html?highlight=setStatusFramePeriod
        controller.setStatusFramePeriod(StatusFrame.Status_1_General, 100);
        controller.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 100);
    }

    public WPI_TalonSRX createFrontLifterMotorController() {
        WPI_TalonSRX controller = new WPI_TalonSRX(CanDevice.MC_CLIMB_ELEVATOR_FRONT.deviceNum);
        configureClimberLifterMotorController(controller, false);
        return controller;
    }

    public WPI_TalonSRX createRearLifterMotorController() {
        WPI_TalonSRX controller = new WPI_TalonSRX(CanDevice.MC_CLIMB_LIFTER_REAR.deviceNum);
        configureClimberLifterMotorController(controller, false);
        return controller;
    }

    private void configureClimberLifterMotorController(TalonSRX liftMotorControllerFront, boolean isInverted) {
        // Reset to defaults to avoid unexpected behavior from previous runs.
        liftMotorControllerFront.configFactoryDefault(20);
        liftMotorControllerFront.setInverted(isInverted);
        liftMotorControllerFront.setNeutralMode(NeutralMode.Brake);

        // Adjust the update rates of important things. Use half the robot period to maximize chances of getting an
        // update on every robot tick.
        liftMotorControllerFront.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2 ); // default 20

        // Decrease some things on the follower that aren't important in that context. This helps offset the bandwidth
        // cost of increasing important things.
        liftMotorControllerFront.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 100); // default 10
    }

    public WPI_TalonSRX createLifterDriveMotorController() {
        WPI_TalonSRX controller = new WPI_TalonSRX(CanDevice.MC_CLIMB_DRIVE.deviceNum);

        // Just configure it here since there there is only one drive motor controller.
        controller.configFactoryDefault(20);
        controller.setInverted(false);
        controller.setNeutralMode(NeutralMode.Coast);

        // Return the configured controller.
        return controller;
    }

    public DoubleSolenoid createClimberFrontLiftLegEngagerSolenoid() {
        return  new DoubleSolenoid(
                PcmDevice.DSOL_CLIMB_FRONT_LEG_ENGAGER_FWD.module,
                PcmDevice.DSOL_CLIMB_FRONT_LEG_ENGAGER_FWD.channel,
                PcmDevice.DSOL_CLIMB_FRONT_LEG_ENGAGER_REV.channel
        );
    }

    public DoubleSolenoid createClimberRearLiftLegEngagerSolenoid() {
        return  new DoubleSolenoid(
                PcmDevice.DSOL_CLIMB_REAR_LEG_ENGAGER_FWD.module,
                PcmDevice.DSOL_CLIMB_REAR_LEG_ENGAGER_FWD.channel,
                PcmDevice.DSOL_CLIMB_REAR_LEG_ENGAGER_REV.channel
        );
    }

}

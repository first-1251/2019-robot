package org.team1251.frc.robot.parts.mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robotCore.parts.sensors.TalonMagEncoder;
import org.team1251.frc.robot.parts.sensors.SensorFactory;

/**
 * The mechanism that drives one side of the drive base.
 *
 * This mechanism is made up of three motor controllers. For the game, one of these (a talonSRX) is leader to
 * the other two (victorSPX) controllers.
 *
 * The actual motors are physically arranged in a triangle pattern identified as "top", "rear", and "front". In
 * the following diagram they are labeled "T", "R", and "F". The FRONT and REAR of the robot are also labeled.
 * 
 * ```
 *   R         ---         F
 *   E        | T |        R
 *   A         ---         O
 *   R      ---   ---      N
 *         | R | | F |     T
 *          ---   ---
 * ```
 */
public class DriveTrain {

    /**
     * Identifies which drive train this instance represents (left or right)
     */
    public enum Identifier {
        LEFT, RIGHT
    }

    /**
     * The top controller.
     */
    private final WPI_TalonSRX topMotorController;

    /**
     * The front controller
     */
    private final WPI_VictorSPX frontMotorController;

    /**
     * The rear controller
     */
    private final WPI_VictorSPX rearMotorController;

    /**
     * Whether or not the motor controllers are operating in follower mode in this drive train.
     */
    private boolean isFollowerMode = true;

    /**
     * The encoder that tracks the behavior of this drive train.
     */
    private final TalonMagEncoder encoder;

    /**
     * Creates a DriveTrain instance for a specific side of the robot.
     *
     * @param identifier Which side of the robot the drive train is on.
     */
    public DriveTrain(Identifier identifier) {
        // All the heavy lifting of part creation will be handled by the factories.
        ControllerFactory controllerFactory = Robot.controllerFactory;
        SensorFactory sensorFactory = Robot.sensorFactory;

        if (identifier == Identifier.LEFT) {
            topMotorController = controllerFactory.createLeftTopDriveTrainController();
            frontMotorController = controllerFactory.createLeftFrontDriveTrainController();
            rearMotorController = controllerFactory.createLeftRearDriveTrainController();

            encoder = sensorFactory.createLeftDriveTrainEncoder(topMotorController);
        } else {
            topMotorController = controllerFactory.createRightTopDriveTrainController();
            frontMotorController = controllerFactory.createRightFrontDriveTrainController();
            rearMotorController = controllerFactory.createRightRearDriveTrainController();

            encoder = sensorFactory.createRightDriveTrainEncoder(topMotorController);
        }

        // Follower mode by default.
        setFollowerMode(true);
    }

    /**
     * Sets the follower mode of the motor controllers within drive train.
     *
     * If the state is the same as the last known state, this is exits as fast as possible.
     *
     * @param state The desired state of follower mode (true = on, false = off).
     */
    private void setFollowerMode(boolean state) {
        // Exit fast if there is no change in state.
        if (state == isFollowerMode) {
            return;
        }

        // State has changed. Record the new state and decide what action to take.
        isFollowerMode = state;
        if (isFollowerMode) {
            // We are turning on follower mode.
            frontMotorController.follow(topMotorController);
            rearMotorController.follow(topMotorController);
        } else {
            // Turn off following by explicitly setting speed on follower controllers
            frontMotorController.set(ControlMode.PercentOutput, 0);
            rearMotorController.set(ControlMode.PercentOutput, 0);
        }
    }

    /**
     * Set power to the entire drive train. This will force the drive train into follower-mode if it isn't
     * already.
     *
     * @param speed A value between -1 and 1 indicating how fast to spin the motor with negative values being
     *              backwards and positive forward.
     */
    public void set(double speed) {
        setFollowerMode(true);
        topMotorController.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Sets the power of ONLY the top motor controller. This will turn off follower mode if it is enabled.
     * It will also stop the other two motors.
     *
     * Useful for testing performance of individual motors.
     *
     * @param speed A value between -1 and 1 indicating how fast to spin the motor with negative values being
     *              backwards and positive forward.
     */
    public void setTop(double speed) {
        setFollowerMode(false);
        topMotorController.set(ControlMode.PercentOutput, speed);
        frontMotorController.set(ControlMode.PercentOutput, 0);
        rearMotorController.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Sets the power of ONLY the front motor controller. This will turn off follower mode if it is enabled.
     * It will also stop the other two motors.
     *
     * Useful for testing performance of individual motors.
     *
     * @param speed A value between -1 and 1 indicating how fast to spin the motor with negative values being
     *              backwards and positive forward.
     */
    public void setFront(double speed) {
        setFollowerMode(false);
        topMotorController.set(ControlMode.PercentOutput, 0);
        frontMotorController.set(ControlMode.PercentOutput, speed);
        rearMotorController.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Sets the power of ONLY the rear motor controller. This will turn off follower mode if it is enabled.
     * It will also stop the other two motors.
     *
     * Useful for testing performance of individual motors.
     *
     * @param speed A value between -1 and 1 indicating how fast to spin the motor with negative values being
     *              backwards and positive forward.
     */
    public void setRear(double speed) {
        setFollowerMode(false);
        topMotorController.set(ControlMode.PercentOutput, 0);
        frontMotorController.set(ControlMode.PercentOutput, 0);
        rearMotorController.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Provides the current velocity of this drive train in encoder units per 100ms
     *
     * @return Velocity of drive train in encoder units per 100ms.
     */
    public double getVelocity() {
        return encoder.getVelocity();
    }
}

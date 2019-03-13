package org.team1251.frc.robot.humanInterface.input;

import edu.wpi.first.wpilibj.Timer;
import org.team1251.frc.robot.DrivePower;
import org.team1251.frc.robotCore.util.ValueSmoother;

public class TigerDriveInput implements HumanDriveInput{

    /**
     * Used to calculate the turn power based on turn and throttle inputs.
     *
     * At full throttle and full turn, this is the difference in power between the left and
     * right sides of the drive train.
     *
     * For two input samples with the full turn value, the one with less throttle input will result
     * in a drive power difference which is HIGHER than this value.
     *
     * For two input samples with full throttle value, the one with less turn input will result
     * in a drive power difference which is LOWER than this value.
     */
    private double turnSensitivity = .85;

    /**
     * Scale factor for quick turns.
     */
    private static double QUICK_TURN_SENSITIVITY = .5;

    /**
     * The minimum reverse throttle input value required to invert the quick-turn direction. Quick-turn is inverted when
     * going backwards to make the motion consistent with regular turning while moving backwards.
     *
     * This should always be a negative value since it represents an amount of reverse throttle power. The primary use
     * for this is as a "noise filter", so practical values are slightly below 0.
     *
     * A value of zero (or any positive value) will invert the quick-turn direction if *any* reverse throttle input
     * value is detected. A value of -1 (or less) will *never* invert quick turn direction.
     */
    private double reverseQuickturnThreshold = -999999; // effectively disabled. Old value = -.10

    /**
     * The minimum turn input value (in either direction) required for quick-turns.
     *
     * This should always be a positive value since it represents the turn inputs distance from 0. The primary use
     * for is to filter out "noise" and values that would not turn the robot due to friction. The perfect value is:
     *   - High enough to ignore input "noise"
     *   - High enough to ignore turn values which will overcome friction (wasted motor power)
     *   - Low enough to achieve the minimum desired quick-turn speed.
     *
     * A value of zero (or any negative value) will always allow a quick turn. A value of 1 (or higher) will never
     * allow a quick turn.
     */
    private double quickTurnThreshold = .15;

    /**
     * The amount of implicit throttle to apply for turning when no other throttle value is available.
     */
    private static double IMPLICIT_THROTTLE_FOR_TURNING = .3;

    /**
     * A hysteresis (in the form of a 10 sample average) used to ramp down throttle when the driver continues to apply
     * turn power after releasing throttle.
     *
     * This hysteresis auto-resets if it is not fed within .10 seconds or if it is fed a value which changes signs from
     * the previously fed value.
     */
    private ValueSmoother throttleHysteresis = new ValueSmoother(10, .10, true);

    /**
     * The most recently applied throttle value. Used to avoid abrupt changes in throttle signs when applying
     * implicit throttle.
     */
    private Double previousThrottleValue = null;

    /**
     * A timer which is reset every time an throttle is calculated. This is used to make stale values are not used
     * to influence implicit throttle values.
     *
     * @see #PREVIOUS_THROTTLE_VALUE_TIMEOUT
     */
    private final Timer previousThrottleExpiryTimer = new Timer();

    /**
     * The amount of time (seconds) before the lastImplicitThrottle is considered "stale" and discarded.
     */
    private static double PREVIOUS_THROTTLE_VALUE_TIMEOUT = .1;

    /**
     * Local representation of interpreted input values.
     */
    class InputValues {
        /**
         * Value between -1 and 1 representing throttle input value where -1 is full speed backwards and 1 is
         * full speed forward.
         */
        final double throttle;

        /**
         * Value between -1 and 1 representing turning input value where -1 is full left turn and 1 is full
         * right turn.
         */
        final double turn;

        /** Represents whether or not a quick turn has been activated. */
        final boolean isQuickTurn;

        final double hysteresisThrottle;


        InputValues(double throttle, double turn, boolean isQuickTurn) {
            this.throttle = throttle;
            this.turn = turn;
            this.isQuickTurn = isQuickTurn;
            this.hysteresisThrottle = 0;
        }

        @Override
        public String toString() {
            return throttle + "|" + turn + "|" + (isQuickTurn ? "true" : "false");
        }
    }

    /**
     * Default constructor
     */
    TigerDriveInput() {
        previousThrottleExpiryTimer.start();
    }

    /**
     * Helper method for packaging throttle, turn, and quick turn inputs into an InputValues instance.
     *
     * @param humanInput The source of the input values.
     */
    private InputValues getInputValues(HumanInput humanInput, double leftVelocity, double rightVelocity) {

        // Get the direct throttle/turn inputs.
        double throttle = humanInput.getDriverPad().ls().getVertical();
        double hysteresisThrottle = throttleHysteresis.getSmoothedValue(throttle);

        // Net trigger value for quick turn power. If it is high enough, trigger a quick turn.
        double quickTurnPower = humanInput.getDriverPad().lt().getValue() - humanInput.getDriverPad().rt().getValue();
        if (Math.abs(quickTurnPower) > quickTurnThreshold) {
            return new InputValues(throttle, quickTurnPower * QUICK_TURN_SENSITIVITY, true);
        }

        // Not a quick turn. Look at the stick for turn power.
        double turn = humanInput.getDriverPad().rs().getHorizontal();

        // See if we need to force a throttle value to handle cases where the driver is using the turn stick
        // without any throttle.
        if (Math.abs(turn) > 0.05 && Math.abs(throttle) < .05) {
            // Okay... the driver clearly wants to turn. Let's give them some power to do it.

            // Look at the hysteresis to see if they let go of the throttle recently enough to give them more power
            // than the static implicit throttle value.
            if (Math.abs(hysteresisThrottle) > IMPLICIT_THROTTLE_FOR_TURNING) {
                // The driver just recently let go of the throttle, but they are still trying to turn -- in other
                // words they are coasting into a turn. Use the Hysteresis to ramp the turn motion down.
                throttle = hysteresisThrottle;
            } else {
                // They let go of the throttle a while ago, but they clearly still want to turn. Give them
                // a little juice. See if there is a valid, previous throttle value that needs to be considered.
                if (previousThrottleValue != null && previousThrottleExpiryTimer.get() < PREVIOUS_THROTTLE_VALUE_TIMEOUT) {
                    // Keep the sign of the last throttle value so that the robot does not abruptly change direction.
                    throttle = previousThrottleValue < 0 ? -IMPLICIT_THROTTLE_FOR_TURNING : IMPLICIT_THROTTLE_FOR_TURNING;
                } else {
                    // No previous throttle that we need to inspect, so just apply the minimum.
                    throttle = IMPLICIT_THROTTLE_FOR_TURNING;
                }
            }
        }

        previousThrottleValue = throttle;
        previousThrottleExpiryTimer.reset();
        return new InputValues(throttle, turn, false);
    }

    @Override
    public DrivePower getDrivePower(HumanInput humanInput, double leftVelocity, double rightVelocity) {
        InputValues input = getInputValues(humanInput, leftVelocity, rightVelocity);

        // Calculate the maximum power adjustment to be made to the right and left drive trains based on driver inputs.
        // Less throttle (in either direction) or less turn will result in a smaller value. This adjustment is added to
        // one side of the drive base and removed from the other. The sign of the adjustment is based on the sign of the
        // turn input (e.g. if turning left, the adjustment will be negative).
        //
        // NOTE: Even though this makes it look like you get more dramatic turns at high speed, that is not the case! See
        // more details on that point down below where the adjustment is made.
        double turningPowerAdjustment = input.turn * input.throttle * turnSensitivity;

        // See if this is a quick-turn scenario.
        if (input.isQuickTurn) {
            // If the driver is trying to go backwards, then invert the quick-turn direction to keep things
            // more natural.
            double quickTurn = input.throttle < reverseQuickturnThreshold ? -input.turn : input.turn;

            // Set the sides of the drive train to exact opposites of each other.
            return new DrivePower(-quickTurn, quickTurn);
        }

        // Apply the adjustment. Always add it to the right side and remove it from the left side. The sign of the adjustment
        // will account for turn direction.
        //
        // NOTE: At higher throttles, one side will exceed the 1,-1 boundary. This will DECREASE the total difference between
        //       the sides of the drive trains, thus producing LESS dramatic turns at higher throttle. By contrast, the slower
        //       drive train never drops below zero (even at low throttle) because the turn adjustment power can never exceed
        //       the turn throttle since the formula is `turn * throttle` -- at a turn value of 1 (maximum), the turning
        //       adjustment would be equal to the throttle.
        return new DrivePower(input.throttle + turningPowerAdjustment, input.throttle - turningPowerAdjustment);
    }

    /**
     * Applies a curve to an input value (as seen here: https://www.desmos.com/calculator/scr5agfmia).
     *
     * @param value The raw input value.
     * @param dampeningStrength A value between 0 and 1. A lower value applies less dampening to low input values.
     * @param dampeningRangeFactor Dampening applies heavier to lower input values. A higher dampeningRangeFactor causes
     *                             heavier dampening to apply to a wider range of input values. At a value of 1 the
     *                             range is empty thus no dampening occurs. With a maximum dampeningStrength (1):
     *                                - A dampeningRangeFactor of 2 reduces an input of .05 to about .25
     *                                - A dampeningRangeFactor of 3 reduces an input of .05 to about .125
     *                                - A dampeningRangeFactor of 9 reduces an input of .05 to below .002
     */
    private double applyInputCurve(double value, double dampeningStrength, int dampeningRangeFactor) {
        // Do the math, preserve the original sign.
        double absValue = Math.abs(value);

        double refined = (dampeningStrength * Math.pow(absValue, dampeningRangeFactor)) + ((1 - dampeningStrength) * absValue);
        return value < 0 && refined > 0 ? -refined : refined;
    }
}

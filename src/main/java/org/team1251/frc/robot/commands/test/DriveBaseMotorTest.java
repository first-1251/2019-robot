package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.DriveBase;

/**
 * A command used to test each motor, one at a time.
 *
 * This command will cycle through each motor one at a time, running each for 1000 ticks (about 10 seconds) before
 * advancing to the next one.
 *
 * There is a 1000 tick delay at the start of the command and again after the final motor in the test before starting
 * the sequence over again.
 */
public class DriveBaseMotorTest extends Command {

    /**
     * How long (seconds) to rest between each motor.
     */
    private static final double REST_PERIOD = .5;

    /**
     * How long (seconds) to run the motor being tested.
     */
    private static final double TEST_PERIOD = .5;

    /**
     * Whether or not a rest period is active.
     */
    private boolean isResting = false;

    /**
     * Timer used to measure how long rests and tests have been underway.
     */
    private final Timer timer = new Timer();

    /**
     * The driveBase is the subsystem which provides access to the motors.
     */
    private final DriveBase driveBase;

    /**
     * The motor which is currently being tested.
     */
    private DriveBase.Motor currentMotor;



    /**
     * Creates an instance.
     *
     * @param driveBase The subsystem that provides access to the motors.
     */
    public DriveBaseMotorTest(DriveBase driveBase) {
        // The drive base is a required subsystem.
        this.driveBase = driveBase;
        requires(driveBase);
    }

    /**
     * Resets the test.
     */
    public void reset(DriveBase.Motor startingMotor) {
        isResting = true;
        currentMotor = startingMotor;

        timer.reset();
    }

    @Override
    protected void initialize() {
        timer.start();
    }

    private void stopAllTests() {
        driveBase.testMotor(null);
    }

    @Override
    protected void end() {
        // stop but don't reset -- we'll pick up where we left off on next execution unless somebody
        // explicitly calls reset() before then.
        timer.stop();
        stopAllTests();
    }

    @Override
    protected void execute() {
        // Decide which motor we should be testing right now and initiate the test.
        selectMotor();
        driveBase.testMotor(currentMotor);
    }

    @Override
    protected boolean isFinished() {
        // Never finishes on its own. Just keep cycling through motors.
        return false;
    }

    /**
     * Advances to the next motor to be tested and resets the elapsed test ticks.
     *
     * After the last motor in the sequence, the motor is set to `null` providing a rest period.
     */
    private void selectMotor() {

        // See if we are resting
        if (isResting) {
            System.out.println("Motor Test resting");
            if (!timer.hasPeriodPassed(REST_PERIOD)) {
                System.out.println("Still Resting");
                return;
            }

            // rest time is over!
            System.out.println("Last moment of resting.");
            isResting = false;
            timer.reset();
            return;
        }

        // If there is no motor selected or the test duration has elapsed for the selected motor, move to the next one.
        if (currentMotor == null || timer.hasPeriodPassed(TEST_PERIOD)) {
            advanceMotor();
            System.out.println("Advanced to next motor: " + currentMotor.name());
        }
    }

    private void advanceMotor() {

        // We always activate the rest period when advancing motors.
        isResting = true;
        timer.reset();

        // See if there is a current motor.
        if (currentMotor == null) {
            // No current motor, start at the beginning.
            currentMotor = DriveBase.Motor.LEFT_TOP;
            return;
        }

        // There is a current motor, advance to the next one in the sequence.
        switch (currentMotor) {
            case LEFT_TOP:
                currentMotor = DriveBase.Motor.LEFT_FRONT;
                break;
            case LEFT_FRONT:
                currentMotor = DriveBase.Motor.LEFT_REAR;
                break;
            case LEFT_REAR:
                currentMotor = DriveBase.Motor.RIGHT_TOP;
                break;
            case RIGHT_TOP:
                currentMotor = DriveBase.Motor.RIGHT_FRONT;
                break;
            case RIGHT_FRONT:
                currentMotor = DriveBase.Motor.RIGHT_REAR;
                break;
            case RIGHT_REAR:
                currentMotor = null;
                break;
        }
    }
}

package org.team1251.frc.robot.commands.test;

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
public class MotorTest extends Command {

    /**
     * The number of ticks to test each motor.
     */
    private final static int TEST_DURATION = 1000;

    /**
     * The number of ticks that the test has been running for a given motor.
     */
    private int elapsedTestTicks = 0;

    /**
     * The driveBase is the subsystem which provides access to the motors.
     */
    private final DriveBase driveBase;

    /**
     * The motor which is currently being tested or `null` if no motor is being tested.
     */
    private DriveBase.Motor currentMotor = null;

    /**
     * Creates an instance.
     *
     * @param driveBase The subsystem that provides access to the motors.
     */
    public MotorTest(DriveBase driveBase) {

        // The drive base is a required subsystem.
        this.driveBase = driveBase;
        requires(driveBase);
    }

    /**
     * Resets the test.
     */
    public void reset() {
        elapsedTestTicks = 0;
        currentMotor = null;
    }

    @Override
    protected void end() {
        driveBase.testMotor(null);
    }

    @Override
    protected void execute() {

        // Keep track of how many ticks have passed while testing is active then combine it with the tick period to
        // derive a time-based measure of how much testing time has passed.
        elapsedTestTicks++;
        if (elapsedTestTicks >= TEST_DURATION) {
            elapsedTestTicks = 0;
            advanceMotor();
        }

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
    private void advanceMotor() {

        // New motor, reset elapsed ticks.
        this.elapsedTestTicks = 0;

        // Figure out which motor to run next.
        if (currentMotor == null) {
            // No current motor, select the first one in the sequence.
            currentMotor = DriveBase.Motor.LEFT_TOP;
            return;
        }

        // There is a current motor, advance to the next one in the sequence.
        switch (currentMotor) {

            case LEFT_TOP:
                currentMotor = DriveBase.Motor.LEFT_BOTTOM_FRONT;
                break;

            case LEFT_BOTTOM_FRONT:
                currentMotor = DriveBase.Motor.LEFT_BOTTOM_REAR;
                break;

            case LEFT_BOTTOM_REAR:
                currentMotor = DriveBase.Motor.RIGHT_TOP;
                break;

            case RIGHT_TOP:
                currentMotor = DriveBase.Motor.RIGHT_BOTTOM_FRONT;
                break;

            case RIGHT_BOTTOM_FRONT:
                currentMotor = DriveBase.Motor.RIGHT_BOTTOM_REAR;
                break;

            case RIGHT_BOTTOM_REAR:
                currentMotor = null; // Rest period after we've ran all motors.
        }

    }
}

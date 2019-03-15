package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.*;

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

    private static final double REST_PERIOD = .25;

    private boolean isResting = false;

    private final Timer timer = new Timer();

    /**
     * The driveBase is the subsystem which provides access to the motors.
     */
    private final DriveBase driveBase;
    private final Arm arm;
    private final CargoCollector collector;
    private final Climber climber;
    private final ManipulatorElevator elevator;

    /**
     * The motor which is currently being tested.
     */
    private Motor currentMotor = Motor.NONE;

    public enum Motor {
        NONE(.3), // Add in a little extra bit of nothing at the start of each cycle. pause = testDuration + (rest * 2)
        DRIVE_LEFT_TOP,
        DRIVE_LEFT_BOTTOM_FRONT,
        DRIVE_LEFT_BOTTOM_REAR,
        DRIVE_RIGHT_TOP,
        DRIVE_RIGHT_BOTTOM_FRONT,
        DRIVE_RIGHT_BOTTOM_REAR,

        LIFT_FRONT,
        LIFT_REAR,
        LIFT_DRIVE,

        ARM,
        CARGO_COLLECTOR,
        ELEVATOR;

        public final double testDuration;

        Motor() {
            this(.25);
        }

        Motor(double testDuration) {
            this.testDuration = testDuration;
        }
    }

    /**
     * Creates an instance.
     *
     * @param driveBase The subsystem that provides access to the motors.
     */
    public MotorTest(DriveBase driveBase, Arm arm, CargoCollector collector, Climber climber, ManipulatorElevator elevator) {
        // The drive base is a required subsystem.

        this.driveBase = driveBase;
        this.arm = arm;
        this.collector = collector;
        this.climber = climber;
        this.elevator = elevator;

        requires(driveBase);
        requires(arm);
        requires(collector);
        requires(climber);
    }

    /**
     * Resets the test.
     */
    public void reset(Motor startingMotor) {
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
        arm.stopArmMotor();
        collector.stopCargoMotor();
        climber.stopMotorControllerRear();
        climber.stopMotorControllerFront();
        climber.stopDriveMotorController();
        elevator.stopMotorController();

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


        // Keep track of how many ticks have passed while testing is active then combine it with the tick period to
        // derive a time-based measure of how much testing time has passed.
        selectMotor();

        // Stop all of the motor tests, then start the right one back up.
        // TODO: Is this off/on cycle fast enough to not be noticed?
        stopAllTests();

        switch(currentMotor) {

            case NONE:
                break;
            case DRIVE_LEFT_TOP:
                driveBase.testMotor(DriveBase.Motor.LEFT_TOP);
                break;
            case DRIVE_LEFT_BOTTOM_FRONT:
                driveBase.testMotor(DriveBase.Motor.LEFT_BOTTOM_FRONT);
                break;
            case DRIVE_LEFT_BOTTOM_REAR:
                driveBase.testMotor(DriveBase.Motor.LEFT_BOTTOM_REAR);
                break;
            case DRIVE_RIGHT_TOP:
                driveBase.testMotor(DriveBase.Motor.RIGHT_TOP);
                break;
            case DRIVE_RIGHT_BOTTOM_FRONT:
                driveBase.testMotor(DriveBase.Motor.RIGHT_BOTTOM_FRONT);
                break;
            case DRIVE_RIGHT_BOTTOM_REAR:
                driveBase.testMotor(DriveBase.Motor.RIGHT_BOTTOM_REAR);
                break;
            case LIFT_FRONT:
                 climber.testMotorControllerFront();
                break;
            case LIFT_REAR:
                 climber.testMotorControllerRear();
                break;
            case LIFT_DRIVE:
                 climber.testDriveMotorController();
                break;
            case ARM:
                // arm.testArmMotor();
                break;
            case CARGO_COLLECTOR:
                // collector.testCargoMotor();
                break;
            case ELEVATOR:
                //  elevator.testMotorController();
                break;
        }
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

            System.out.println("Last moment of resting.");
            // rest time is over!
            isResting = false;
            timer.reset();
            return;
        }

        // If there is no motor selected or the test duration has elapsed for the selected motor, move to the next one.
        if (currentMotor == null || timer.hasPeriodPassed(currentMotor.testDuration)) {
            String nextMotorName = nextMotor().name();
            System.out.println("Moving to next motor: " + nextMotorName);
        }
    }

    private Motor nextMotor() {

        // We always activate the rest period when advancing motors.
        isResting = true;
        timer.reset();

        // Handle first motor.
        if (currentMotor == null) {
            currentMotor = Motor.NONE;
        }

        // There is a current motor, advance to the next one in the sequence.
        switch (currentMotor) {
            case NONE:
                currentMotor = Motor.DRIVE_LEFT_TOP;
                break;
            case DRIVE_LEFT_TOP:
                currentMotor = Motor.DRIVE_LEFT_BOTTOM_FRONT;
                break;
            case DRIVE_LEFT_BOTTOM_FRONT:
                currentMotor = Motor.DRIVE_LEFT_BOTTOM_REAR;
                break;
            case DRIVE_LEFT_BOTTOM_REAR:
                currentMotor = Motor.DRIVE_RIGHT_TOP;
                break;
            case DRIVE_RIGHT_TOP:
                currentMotor = Motor.DRIVE_RIGHT_BOTTOM_FRONT;
                break;
            case DRIVE_RIGHT_BOTTOM_FRONT:
                currentMotor = Motor.DRIVE_RIGHT_BOTTOM_REAR;
                break;
            case DRIVE_RIGHT_BOTTOM_REAR:
                currentMotor = Motor.LIFT_FRONT;
                break;
            case LIFT_FRONT:
                currentMotor = Motor.LIFT_REAR;
                break;
            case LIFT_REAR:
                currentMotor = Motor.LIFT_DRIVE;
                break;
            case LIFT_DRIVE:
                currentMotor = Motor.ELEVATOR;
                break;
            case ELEVATOR:
                currentMotor = Motor.ARM;
                break;
            case ARM:
                currentMotor = Motor.CARGO_COLLECTOR;
                break;
            case CARGO_COLLECTOR:
                currentMotor = Motor.DRIVE_LEFT_TOP;
                break;
        }

        return currentMotor;
    }
}

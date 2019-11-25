package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;
import org.team1251.frc.robot.subsystems.DriveBase;

public class Climb extends Command {

    private enum ClimbPhase {
        INITIALIZING, // Initializing
        LIFTING,  // Raising with elevators
        GAINING_FOOTHOLD, // Driving forward until front wheel is on platform
        RETRACTING_FRONT, // Retracting front elevator
        GAINING_BALANCE, // Driving forward (drive base) until robot no longer needs legs
        RETRACTING_BACK, // Retracting rear elevator
        FINISHING, // Driving forward (drive base) until completely on platform.
        ALL_THE_POINTS,
        ABANDON
    }

    /**
     * How many seconds to drive forward while finishing the climb. This should be enough to get the
     * rest of the robot onto the platform.
     */
    private final double FINISHING_DRIVE_DURATION = .10;

    private Timer finishingTimer;

    private final DriveBase driveBase;
    private final Climber climber;
    private final Climber.LiftTarget target;

    private ClimbPhase currentPhase;

    public Climb(DriveBase driveBase, Climber climber, Climber.LiftTarget target) {
        this.driveBase = driveBase;
        this.climber = climber;
        this.target = target;
        requires(driveBase);
        requires(climber);

        this.setInterruptible(false);
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        currentPhase = ClimbPhase.INITIALIZING;
        this.finishingTimer = null;
    }

    public void abandon() {
        if (currentPhase == null) {
            // Never initialized -- nothing to abandon.
            return;
        }

        // Can not abandon once we've starting leg retractions.
        if (currentPhase.ordinal() >= ClimbPhase.RETRACTING_FRONT.ordinal()) {
            return;
        }

        // Immediately advance phase to "abandon".
        currentPhase = ClimbPhase.ABANDON;
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        System.out.println("Previous Phase:" + currentPhase.name());
        advancePhase();
        System.out.println("next phase" + currentPhase.name());
        switch (currentPhase) {
            case ABANDON:
                // Stop lift motors, stop the lift drive motor, and initiate retracts.
                climber.killLiftMotors();
                climber.stopDrive();
                climber.retractFrontLeg();
                climber.retractRear();
                break;
            case INITIALIZING:
                break;
            case LIFTING:
                climber.liftTo(target);
                break;
            case GAINING_FOOTHOLD:
                climber.liftTo(target);
                climber.startDrive();
                break;
            case RETRACTING_FRONT:
                climber.stopDrive();
                climber.retractFrontLeg();
                climber.relieveFrontPressure();
                break;
            case GAINING_BALANCE:
                // Go back to sustaining current height and drive forward with the main drive base now that we
                // have wheels on the platform.
                climber.sustain();
                driveBase.drive(.25);
                break;
            case RETRACTING_BACK:
                // Kill the motors (so there isn't pressure on the leg engager) and issue a retract for the rear leg.
                climber.killLiftMotors();
                climber.retractRear();
                break;
            case FINISHING:
                if (finishingTimer == null) {
                    finishingTimer = new Timer();
                    finishingTimer.reset();
                    finishingTimer.start();
                }

                driveBase.drive(.25);
                break;
            case ALL_THE_POINTS:
                driveBase.drive(0);
                if (finishingTimer != null) {
                    finishingTimer.stop();
                    finishingTimer = null;
                }
                break;
        }
    }

    private void advancePhase() {

        switch(currentPhase) {
            case ABANDON:
                // See if both lifters are fully retracted.
                if (climber.isFrontLegRetracted() && climber.isRearLegRetracted()) {
                    // Done. We'll advance to ALL_THE_POINTS (even though we did not get all the points)
                    // so that the command will identify itself as being "finished".
                    currentPhase = ClimbPhase.ALL_THE_POINTS;
                }

                break;
            case INITIALIZING:
                currentPhase = ClimbPhase.LIFTING;
                break;
            case LIFTING:
                if (climber.hasReachedLiftTarget()) {
                    currentPhase = ClimbPhase.GAINING_FOOTHOLD;
                }
                break;
            case GAINING_FOOTHOLD:
                if (climber.isFrontOnSolidGround()) {
                    currentPhase = ClimbPhase.RETRACTING_FRONT;
                }

                break;
            case RETRACTING_FRONT:
                // TODO: Can we consider this complete sooner?
                if (climber.isFrontLegRetracted()) {
                    currentPhase = ClimbPhase.GAINING_BALANCE;
                }

                break;
            case GAINING_BALANCE:
                if (climber.isRearOnSolidGround()) {
                    currentPhase = ClimbPhase.RETRACTING_BACK;
                }

                break;
            case RETRACTING_BACK:
                if (climber.isRearLegRetracted()) {
                    currentPhase = ClimbPhase.FINISHING;
                }

                break;
            case FINISHING:
                if (finishingTimer != null && finishingTimer.hasPeriodPassed(FINISHING_DRIVE_DURATION)) {
                    currentPhase = ClimbPhase.ALL_THE_POINTS;
                }

                break;
            case ALL_THE_POINTS:
                break;
        }
    }

    @Override
    protected boolean isFinished() {
        return currentPhase == ClimbPhase.ALL_THE_POINTS;
    }
}

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
    private final Climber.Target target;

    private ClimbPhase currentPhase;

    public Climb(DriveBase driveBase, Climber climber, Climber.Target target) {
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
        // Can not abandon once lift has completed.
        if (currentPhase.ordinal() > ClimbPhase.LIFTING.ordinal()) {
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
        advancePhase();
        switch (currentPhase) {
            case ABANDON:
                // Kill the climb motors and the drive motor
                climber.drive(0);
                climber.kill();

                // Once a climb elevator is within 2.5 inches of retracted (.5 inch off ground), disengage it.
                if (climber.getElevatorRearEncoder().getDistance() < 2.5) {
                    climber.getElevatorRearEngager().setState(false);
                }

                if (climber.getElevatorFrontEncoder().getDistance() < 2.5) {
                    climber.getElevatorFrontEngager().setState(false);
                }

                break;

            case INITIALIZING:
                break;
            case LIFTING:
                climber.lift(target);
                break;
            case GAINING_FOOTHOLD:
                climber.lift(target);
                climber.drive(1);
                break;
            case RETRACTING_FRONT:
                climber.drive(0);
                climber.retractFront();
                climber.relieveFrontPressure();
                break;
            case GAINING_BALANCE:
                driveBase.drive(.25);
                break;
            case RETRACTING_BACK:
                driveBase.drive(0);
                climber.kill();
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
                finishingTimer.stop();
                finishingTimer = null;
                break;
        }
    }

    private void advancePhase() {

        switch(currentPhase) {
            case ABANDON:
                // See if both elevators are fully retracted.
                if (climber.isFrontElevatorRetracted() && climber.isRearElevatorRetracted()) {
                    // Done. We'll call this ALL_THE_POINTS even though we did not get all the points.
                    currentPhase = ClimbPhase.ALL_THE_POINTS;
                }

                break;
            case INITIALIZING:
                currentPhase = ClimbPhase.LIFTING;
                break;
            case LIFTING:
                if (climber.isLifted()) {
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
                if (climber.isFrontElevatorRetracted()) {
                    currentPhase = ClimbPhase.GAINING_BALANCE;
                }

                break;
            case GAINING_BALANCE:
                if (climber.isRearOnSolidGround()) {
                    currentPhase = ClimbPhase.RETRACTING_BACK;
                }

                break;
            case RETRACTING_BACK:
                if (climber.isRearElevatorRetracted()) {
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

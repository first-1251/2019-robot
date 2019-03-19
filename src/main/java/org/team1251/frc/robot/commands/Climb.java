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
        ALL_THE_POINTS
    }

    /**
     * How many seconds to drive forward while finishing the climb. This should be enough to get the
     * rest of the robot onto the platform.
     */
    private final double FINISHING_DRIVE_DURATION = .10;

    private Timer finishingTimer;

    private final DriveBase driveBase;
    private final Climber climber;

    private ClimbPhase currentPhase;

    public Climb(DriveBase driveBase, Climber climber) {
        this.driveBase = driveBase;
        this.climber = climber;
        requires(driveBase);
        requires(climber);
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {

        currentPhase = ClimbPhase.INITIALIZING;
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        advancePhase();
        switch (currentPhase) {
            case INITIALIZING:
                break;
            case LIFTING:
                climber.lift();
                break;
            case GAINING_FOOTHOLD:
                climber.lift();
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

//                driveBase.drive(.20);
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

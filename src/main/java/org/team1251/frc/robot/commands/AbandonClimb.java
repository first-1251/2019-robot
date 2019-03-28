package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A special command that acts on another command to short-circuit it!
 */
public class AbandonClimb extends Command {

    private boolean wasAbandonCalled;
    private final Climb climbLvl2;
    private final Climb climbLvl3;

    public AbandonClimb(Climb climbLvl2, Climb climbLvl3) {
        this.climbLvl2 = climbLvl2;
        this.climbLvl3 = climbLvl3;
    }

    @Override
    protected void initialize() {
        wasAbandonCalled = false;
    }

    @Override
    protected void execute() {
        // Abandon all the climbs.. don't bother to find out if either is running.
        climbLvl2.abandon();
        climbLvl3.abandon();
        wasAbandonCalled = true; // Only need to call it once.
    }

    @Override
    protected boolean isFinished() {
        return wasAbandonCalled;
    }
}

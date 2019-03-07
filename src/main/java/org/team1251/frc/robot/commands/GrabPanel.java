package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Grappler;

public class GrabPanel extends Command {

    private enum Phase {
        INITIALIZING,
        UNCLAMP,
        UNCLAMP_WAIT,
        EXTEND,
        EXTEND_WAIT,
        CLAMP,
        CLAMP_WAIT,
        RETRACT,
        I_CAN_HAS_PANEL
    }

    private final static double UNCLAMP_WAIT_PERIOD = 0.1;
    private final static double EXTEND_WAIT_PERIOD = 0.1;
    private final static double CLAMP_WAIT_PERIOD = 0.1;

    private final Grappler grappler;
    private Phase currentPhase;
    private Timer waitTimer = new Timer();


    @Override
    protected void initialize() {
        currentPhase = Phase.INITIALIZING;
    }

    public GrabPanel(Grappler grappler) {
        this.grappler = grappler;
        requires(this.grappler);
    }

    @Override
    protected void execute() {
        advancePhase();

        // Do work in phases which require work. ("Wait phases don't require work")
        switch(currentPhase) {

            case UNCLAMP:
                grappler.unclamp();
                waitTimer.start();
                waitTimer.reset();
                break;

            case EXTEND:
                grappler.extend();
                waitTimer.reset();
                break;

            case CLAMP:
                grappler.clamp();
                waitTimer.reset();
                break;

            case RETRACT:
                grappler.retract();
                break;

        }
    }

    private void advancePhase() {
        switch (currentPhase) {

            case INITIALIZING:
                currentPhase = Phase.UNCLAMP;
                break;

            case UNCLAMP:
                currentPhase = Phase.UNCLAMP_WAIT;
                break;

            case UNCLAMP_WAIT:
                if (waitTimer.hasPeriodPassed(UNCLAMP_WAIT_PERIOD)) {
                    currentPhase = Phase.EXTEND;
                }
                break;

            case EXTEND:
                currentPhase = Phase.EXTEND_WAIT;
                break;

            case EXTEND_WAIT:
                if (waitTimer.hasPeriodPassed(EXTEND_WAIT_PERIOD)) {
                    currentPhase = Phase.CLAMP;
                }
                break;

            case CLAMP:
                currentPhase = Phase.CLAMP_WAIT;
                break;

            case CLAMP_WAIT:
                if (waitTimer.hasPeriodPassed(CLAMP_WAIT_PERIOD)) {
                    currentPhase = Phase.RETRACT;
                }
                break;

            case RETRACT:
                currentPhase = Phase.I_CAN_HAS_PANEL;
                break;

            case I_CAN_HAS_PANEL:
                // Nowhere else to to go.
                break;
        }
    }

    @Override
    protected void interrupted() {
        if (currentPhase != Phase.I_CAN_HAS_PANEL) {
            grappler.clamp();
            grappler.retract();
        }

        end();
    }

    @Override
    protected void end() {
        waitTimer.stop();
    }

    @Override
    protected boolean isFinished() {
        return currentPhase == Phase.I_CAN_HAS_PANEL;
    }
}

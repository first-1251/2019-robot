package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Grappler;

public class PlacePanel extends Command {

    private enum Phase {
        INITIALIZING,
        EXTEND,
        EXTEND_WAIT,
        UNCLAMP,
        UNCLAMP_WAIT,
        RETRACT,
        YOU_CAN_HAS_PANEL
    }

    private final static double EXTEND_WAIT_PERIOD = 0.1;
    private final static double UNCLAMP_WAIT_PERIOD = 0.1;

    private final Grappler grappler;
    private Phase currentPhase;
    private Timer waitTimer = new Timer();


    @Override
    protected void initialize() {
        currentPhase = Phase.INITIALIZING;
    }

    public PlacePanel(Grappler grappler) {
        this.grappler = grappler;
        requires(this.grappler);
    }

    @Override
    protected void execute() {
        advancePhase();

        switch(currentPhase) {

            case EXTEND:
                waitTimer.start();
                grappler.extend();
                waitTimer.reset();
                break;

            case UNCLAMP:
                grappler.unclamp();
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
                currentPhase = Phase.EXTEND;
                break;

            case EXTEND:
                currentPhase = Phase.EXTEND_WAIT;
                break;

            case EXTEND_WAIT:
                if (waitTimer.hasPeriodPassed(EXTEND_WAIT_PERIOD)) {
                    currentPhase = Phase.UNCLAMP;
                }
                break;

            case UNCLAMP:
                currentPhase = Phase.UNCLAMP_WAIT;
                break;

            case UNCLAMP_WAIT:
                if (waitTimer.hasPeriodPassed(UNCLAMP_WAIT_PERIOD)) {
                    currentPhase = Phase.RETRACT;
                }
                break;

            case RETRACT:
                currentPhase = Phase.YOU_CAN_HAS_PANEL;
                break;
        }
    }

    @Override
    protected void interrupted() {
        if (currentPhase != Phase.YOU_CAN_HAS_PANEL) {
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
        return currentPhase == Phase.YOU_CAN_HAS_PANEL;
    }
}

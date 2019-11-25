package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;

public class LiftTest extends Command {

    private final Climber climber;

    public LiftTest(Climber climber) {
        // Once this command is running, it is king until it is finished or has been cancelled. Other climber commands
        // are unsafe once this is running because they expect the climber to be in its starting configuration.
        setInterruptible(false);

        this.climber = climber;
        requires(climber);
    }

    @Override
    protected void end() {
        climber.killLiftMotors();
    }

    @Override
    protected void execute() {
        climber.liftTo(Climber.LiftTarget.TEST);
    }

    @Override
    protected boolean isFinished() {
        return false; // Until the test button is released.
    }
}

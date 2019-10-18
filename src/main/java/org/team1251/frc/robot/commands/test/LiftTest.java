package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;

public class LiftTest extends Command {

    private final Climber climber;

    public LiftTest(Climber climber) {

        this.climber = climber;
        requires(climber);
    }

    @Override
    protected void end() {
        climber.kill();
    }

    @Override
    protected void execute() {
        climber.lift(true, 4); // 5 inch lift for testing.
    }

    @Override
    protected boolean isFinished() {
        return false; // Until the test button is released.
    }
}

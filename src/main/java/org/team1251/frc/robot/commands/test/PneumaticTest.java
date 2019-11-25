package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;
import org.team1251.frc.robotCore.humanInterface.input.Button;

public class PneumaticTest extends Command {

    private final Climber climber;
    private final Button frontLegEngagerButton;
    private final Button rearLegEngagerButton;

    public PneumaticTest(Button frontLegEngagerButton, Button rearLegEngagerButton, Climber climber) {
        this.frontLegEngagerButton = frontLegEngagerButton;
        this.rearLegEngagerButton = rearLegEngagerButton;

        // Once this command is running, it is king until it is finished or has been cancelled. Other climber commands
        // are unsafe once this is running because they expect the climber to be in its starting configuration.
        setInterruptible(false);

        this.climber = climber;
        requires(climber);
    }

    @Override
    protected void end() {
        climber.testFrontLegEngager(false);
        climber.testRearLegEngager(false);
    }

    @Override
    protected void execute() {
        if (frontLegEngagerButton.isPressed()) {
            climber.testFrontLegEngager(true);
        } else {
            climber.testFrontLegEngager(false);
        }

        if (rearLegEngagerButton.isPressed()) {
            climber.testRearLegEngager(true);
        } else {
            climber.testRearLegEngager(false);
        }
    }

    @Override
    protected boolean isFinished() {
        // Never finish on its own.
        return false;
    }
}

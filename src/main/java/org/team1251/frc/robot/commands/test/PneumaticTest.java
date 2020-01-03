package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.XBoxController;

public class PneumaticTest extends Command {


    private final XBoxController testerGamePad;
    private final Climber climber;

    public PneumaticTest(XBoxController testerGamePad, Climber climber) {

        this.testerGamePad = testerGamePad;
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
        if (testerGamePad.a().isPressed()) {
            climber.testFrontLegEngager(true);
        } else {
            climber.testFrontLegEngager(false);
        }

        if (testerGamePad.b().isPressed()) {
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

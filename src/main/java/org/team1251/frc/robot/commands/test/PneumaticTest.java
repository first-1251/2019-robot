package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.GamePad;

public class PneumaticTest extends Command {


    private final GamePad testerGamePad;
    private final Climber climber;

    public PneumaticTest(GamePad testerGamePad, Climber climber) {

        this.testerGamePad = testerGamePad;
        this.climber = climber;
        requires(climber);
    }

    @Override
    protected void end() {
        climber.getElevatorFrontEngager().setState(false);
        climber.getElevatorRearEngager().setState(false);
    }

    @Override
    protected void execute() {
        if (testerGamePad.a().isPressed()) {
            climber.getElevatorFrontEngager().setState(true);
        } else {
            climber.getElevatorFrontEngager().setState(false);
        }

        if (testerGamePad.b().isPressed()) {
            climber.getElevatorRearEngager().setState(true);
        } else {
            climber.getElevatorRearEngager().setState(false);
        }

        if (testerGamePad.x().isPressed()) {
            //grappler.extend();
        } else {
            //grappler.retract();
        }

        if (testerGamePad.y().isPressed()) {
            //grappler.clamp();
        } else {
            //grappler.unclamp();
        }
    }

    @Override
    protected boolean isFinished() {
        // Never finish on its own.
        return false;
    }
}

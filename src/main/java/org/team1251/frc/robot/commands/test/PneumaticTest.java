package org.team1251.frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;
import org.team1251.frc.robot.subsystems.Grappler;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.GamePad;

public class PneumaticTest extends Command {


    private final GamePad testerGamePad;
    private final Climber climber;
    private final Grappler grappler;

    public PneumaticTest(GamePad testerGamePad, Climber climber, Grappler grappler) {

        this.testerGamePad = testerGamePad;
        this.climber = climber;
        this.grappler = grappler;
        requires(climber);
        requires(grappler);
    }

    @Override
    protected void end() {
        climber.getElevatorFrontEngager().setState(true);
        climber.getElevatorRearEngager().setState(true);
        grappler.retract();
        grappler.unclamp();
    }

    @Override
    protected void execute() {
        if (testerGamePad.a().isPressed()) {
            climber.getElevatorFrontEngager().setState(false);
        } else {
            climber.getElevatorFrontEngager().setState(true);
        }

        if (testerGamePad.b().isPressed()) {
            climber.getElevatorRearEngager().setState(false);
        } else {
            climber.getElevatorRearEngager().setState(true);
        }

        if (testerGamePad.x().isPressed()) {
            grappler.extend();
        } else {
            grappler.retract();
        }

        if (testerGamePad.y().isPressed()) {
            grappler.clamp();
        } else {
            grappler.unclamp();
        }
    }

    @Override
    protected boolean isFinished() {
        // Never finish on its own.
        return false;
    }
}

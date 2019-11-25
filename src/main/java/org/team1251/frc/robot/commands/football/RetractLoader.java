package org.team1251.frc.robot.commands.football;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.football.Loader;

public class RetractLoader extends Command {

    private final Loader loader;

    public RetractLoader(Loader loader) {
        this.loader = loader;

        requires(loader);
    }

    @Override
    protected void execute() {
        loader.retract();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

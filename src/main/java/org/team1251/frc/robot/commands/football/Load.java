package org.team1251.frc.robot.commands.football;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.football.Loader;

public class Load extends Command {

    private final Loader loader;

    public Load(Loader loader) {
        this.loader = loader;

        requires(loader);
    }

    @Override
    protected void end() {
        // Deliberately left empty -- leave loader in the "load" state until another command retracts it.
    }

    @Override
    protected void execute() {
        loader.load();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

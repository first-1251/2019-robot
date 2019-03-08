package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.CargoCollector;

public class OuttakeCargo extends Command {

    private final CargoCollector collector;

    public OuttakeCargo(CargoCollector collector){

        this.collector = collector;
        requires(collector);

    }

    @Override
    protected void end() {
        this.collector.stopCollector();
    }

    @Override
    protected void execute() {
        this.collector.eject();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.CargoClarm;

public class OuttakeCargo extends Command {

    private final CargoClarm cargoClarm;

    public OuttakeCargo(CargoClarm cargoClarm){

        this.cargoClarm = cargoClarm;
        requires(cargoClarm);

    }

    @Override
    protected void execute() {
        this.cargoClarm.OuttakeCargo();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.CargoClarm;

public class OutakeCargo extends Command {

    private final CargoClarm cargoClarm;

    public OutakeCargo(CargoClarm cargoClarm){

        this.cargoClarm = cargoClarm;
        requires(cargoClarm);

    }

    @Override
    protected void execute() {
        this.cargoClarm.OutakeCargo();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

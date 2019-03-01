package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.CargoClarm;

public class MoveCargoArmDown extends Command {

    private final CargoClarm cargoClarm;

    public MoveCargoArmDown(CargoClarm cargoClarm){

        this.cargoClarm = cargoClarm;
        requires(cargoClarm);

    }

    @Override
    protected void execute() {
        this.cargoClarm.moveCargoArmDown();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


}

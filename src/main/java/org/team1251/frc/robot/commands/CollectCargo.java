package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team1251.frc.robot.subsystems.CargoClarm;
import org.team1251.frc.robotCore.commands.TimedNothing;

public class CollectCargo extends CommandGroup {

        public CollectCargo (CargoClarm cargoClarm){
            addSequential(new MoveCargoArmDown(cargoClarm));
            addSequential(new TimedNothing(0.5));
            addSequential(new IntakeCargo(cargoClarm));
            addSequential(new TimedNothing(0.25));
            addSequential(new MoveCargoArmUp(cargoClarm));

        }

}

package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ManipulatorElevator;

public class EnableManipulatorElevator extends Command {

    private final ManipulatorElevator manipulatorElevator;

    public EnableManipulatorElevator(ManipulatorElevator manipulatorElevator){

        this.manipulatorElevator = manipulatorElevator;
        requires(manipulatorElevator);

    }

    @Override
    protected void execute() {
        this.manipulatorElevator.EnableManipulatorElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

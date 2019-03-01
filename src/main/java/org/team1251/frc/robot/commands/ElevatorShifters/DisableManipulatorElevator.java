package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ManipulatorElevator;

public class DisableManipulatorElevator extends Command {

    private final ManipulatorElevator manipulatorElevator;

    public DisableManipulatorElevator(ManipulatorElevator manipulatorElevator){
        this.manipulatorElevator = manipulatorElevator;
        requires(manipulatorElevator);
    }

    @Override
    protected void execute() {
//        this.manipulatorElevator.DisableManipulatorElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

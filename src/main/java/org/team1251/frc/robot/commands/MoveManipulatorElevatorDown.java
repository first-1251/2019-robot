package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ManipulatorElevator;

public class MoveManipulatorElevatorDown extends Command {

    private final ManipulatorElevator manipulatorElevator;

    public MoveManipulatorElevatorDown(ManipulatorElevator manipulatorElevator){
        this.manipulatorElevator = manipulatorElevator;
        requires(manipulatorElevator);
    }

    @Override
    protected void execute() {
        this.manipulatorElevator.MoveManipulatorElevatorDown();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}


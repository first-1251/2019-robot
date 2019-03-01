package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ManipulatorElevator;

public class MoveManipulatorElevatorUp extends Command {

    private final ManipulatorElevator manipulatorElevator;

    public MoveManipulatorElevatorUp(ManipulatorElevator manipulatorElevator){
        this.manipulatorElevator = manipulatorElevator;
        requires(manipulatorElevator);
    }

    @Override
    protected void execute() {
        this.manipulatorElevator.MoveManipulatorElevatorUp();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}

package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ManipulatorElevator;

public class MoveElevatorToSetPoint extends Command {


    private final ManipulatorElevator manipulatorElevator;
    private final ManipulatorElevator.SetPoint setPoint;

    public MoveElevatorToSetPoint(ManipulatorElevator manipulatorElevator, ManipulatorElevator.SetPoint setPoint) {

        this.manipulatorElevator = manipulatorElevator;
        this.setPoint = setPoint;
        requires(manipulatorElevator);
    }

    @Override
    protected void execute() {
        manipulatorElevator.moveTo(setPoint);
    }

    @Override
    protected void end() {
        // Let the elevator go into free-fall by setting the motor to neutral.
        manipulatorElevator.neutral();
    }

    @Override
    protected boolean isFinished() {
        // Never automatically finish. Require operator to end the command (i.e. let go of the button).
        return false;
    }
}

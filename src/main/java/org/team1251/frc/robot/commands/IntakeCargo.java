package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Arm;
import org.team1251.frc.robot.subsystems.CargoCollector;
import org.team1251.frc.robot.subsystems.ManipulatorElevatorState;

public class IntakeCargo extends Command {

    private final ManipulatorElevatorState manipulatorElevatorState;
    private final CargoCollector collector;
    private final Arm arm;

    public IntakeCargo(ManipulatorElevatorState manipulatorElevatorState, CargoCollector collector, Arm arm) {
        this.manipulatorElevatorState = manipulatorElevatorState;

        this.collector = collector;
        this.arm = arm;
        requires(collector);
        requires(arm);
    }

    @Override
    protected void execute() {
        collector.collect();
        if (manipulatorElevatorState.isAtBottom()) {
            // Elevator is down... Assume ground pickup, so lower the arm.
            arm.moveCargoArmDown();
        } else {
            // Elevator is not at the bottom, assume human station pickup... keep the arm raised.
            arm.moveCargoArmUp();
        }
    }

    @Override
    protected void end() {
        collector.stopCollector();
    }

    @Override
    protected boolean isFinished() {
        // Run until a piece has been collected.
        return collector.isCargoCollected();
    }
}

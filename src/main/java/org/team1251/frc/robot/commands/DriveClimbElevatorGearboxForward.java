package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Elevators;

public class DriveClimbElevatorGearboxForward extends Command {

    private final Elevators elevators;

    public DriveClimbElevatorGearboxForward(Elevators elevators){
        this.elevators = elevators;
        requires(elevators);
    }

    @Override
    protected void execute() {
        this.elevators.MoveClimbElevatorGearboxForward();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;

public class DisablePanelElevator extends Command {

    private final Elevators elevators;

    public DisablePanelElevator(Elevators elevators){

        this.elevators = elevators;
        requires(elevators);

    }

    @Override
    protected void execute() {
        this.elevators.DisablePanelElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

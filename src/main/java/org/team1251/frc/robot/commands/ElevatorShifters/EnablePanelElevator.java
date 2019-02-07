package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Elevators;

public class EnablePanelElevator extends Command {

    private final Elevators elevators;

    public EnablePanelElevator(Elevators elevators){

        this.elevators = elevators;
        requires(elevators);

    }

    @Override
    protected void execute() {
        this.elevators.EnablePanelElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

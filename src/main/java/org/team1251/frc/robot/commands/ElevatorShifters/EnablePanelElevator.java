package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ManipulatorElevator;

public class EnablePanelElevator extends Command {

    private final ManipulatorElevator manipulatorElevator;

    public EnablePanelElevator(ManipulatorElevator manipulatorElevator){

        this.manipulatorElevator = manipulatorElevator;
        requires(manipulatorElevator);

    }

    @Override
    protected void execute() {
        this.manipulatorElevator.EnablePanelElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

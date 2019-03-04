package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;

public class DisableRearClimbElevator extends Command {

    private final Climber climbElevator;

    public DisableRearClimbElevator(Climber climbElevator){
        this.climbElevator = climbElevator;
        requires(climbElevator);
    }

    @Override
    protected void execute() {
        this.climbElevator.DisableRearClimbElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

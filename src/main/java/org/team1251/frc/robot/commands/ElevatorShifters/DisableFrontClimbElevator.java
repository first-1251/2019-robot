package org.team1251.frc.robot.commands.ElevatorShifters;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ClimbElevator;

public class DisableFrontClimbElevator extends Command {

    private final ClimbElevator climbElevator;

    public DisableFrontClimbElevator(ClimbElevator climbElevator){
        this.climbElevator = climbElevator;
        requires(climbElevator);
    }

    @Override
    protected void execute() {
        this.climbElevator.DisableFrontClimbElevator();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

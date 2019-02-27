package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ClimbElevator;

public class MoveClimbElevatorDown extends Command {

    private final ClimbElevator climbElevator;

    public MoveClimbElevatorDown(ClimbElevator climbElevator){
        this.climbElevator = climbElevator;
        requires(climbElevator);
    }

    @Override
    protected void execute() {
        this.climbElevator.MoveClimbElevatorDown();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}

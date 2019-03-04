package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;

public class MoveClimbElevatorUp extends Command {

    private final Climber climbElevator;

    public MoveClimbElevatorUp(Climber climbElevator){
        this.climbElevator = climbElevator;
        requires(climbElevator);
    }

    @Override
    protected void execute() {
        this.climbElevator.MoveClimbElevatorUp();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}

package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.ClimbElevator;

public class DriveClimbElevatorGearboxForward extends Command {

    private final ClimbElevator climbElevator;

    public DriveClimbElevatorGearboxForward(ClimbElevator climbElevator){
        this.climbElevator = climbElevator;
        requires(climbElevator);
    }

    @Override
    protected void execute() {
        this.climbElevator.MoveClimbElevatorGearboxForward();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

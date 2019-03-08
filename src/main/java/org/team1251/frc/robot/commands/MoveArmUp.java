package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Arm;

public class MoveArmUp extends Command {

    private final Arm arm;

    public MoveArmUp(Arm arm){

        this.arm = arm;
        requires(arm);

    }

    @Override
    protected void end() {
        this.arm.stopArmMotor();
    }

    @Override
    protected void execute() {
        this.arm.moveCargoArmUp();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


}

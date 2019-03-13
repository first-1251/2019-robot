package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.DrivePower;
import org.team1251.frc.robot.humanInterface.input.HumanInput;
import org.team1251.frc.robot.subsystems.DriveBase;

public class TeleopDrive extends Command {

    private final DriveBase driveBase;
    private final HumanInput humanInput;

    public TeleopDrive(DriveBase driveBase, HumanInput humanInput) {
        this.driveBase = driveBase;
        this.humanInput = humanInput;

        this.requires(driveBase);
    }

    @Override
    protected void end() {
        driveBase.drive(new DrivePower(0,0));
    }


    @Override
    protected void execute() {
        driveBase.drive(humanInput.getDrivePower(driveBase.getLeftVelocity(), driveBase.getRightVelocity()));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

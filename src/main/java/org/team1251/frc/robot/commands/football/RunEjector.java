package org.team1251.frc.robot.commands.football;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.football.Ejector;

public class RunEjector extends Command {

    private final Ejector ejector;
    private final NetworkTableEntry leftPowerCtl;
    private final NetworkTableEntry rightPowerCtl;
    private double rightPower = .5;
    private double leftPower = .5;

    public RunEjector(Ejector ejector, NetworkTableEntry leftPowerCtl, NetworkTableEntry rightPowerCtl) {
        this.ejector = ejector;
        requires(ejector);

        this.leftPowerCtl = leftPowerCtl;
        this.rightPowerCtl = rightPowerCtl;
    }

    @Override
    protected void execute() {
        leftPower = leftPowerCtl.getDouble(leftPower);
        rightPower = rightPowerCtl.getDouble(rightPower);

        ejector.run(leftPower, rightPower);
    }

    @Override
    protected void end() {
        ejector.stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

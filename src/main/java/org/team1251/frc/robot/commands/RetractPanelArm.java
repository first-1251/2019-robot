package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.PanelClarm;

public class RetractPanelArm extends Command {

    private final PanelClarm panelClarm;

    public RetractPanelArm(PanelClarm panelClarm){
        this.panelClarm = panelClarm;
        requires(panelClarm);
    }

    @Override
    protected void execute() {
        this.panelClarm.RetractPanelArm();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

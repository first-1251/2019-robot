package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.PanelClarm;

public class OpenPanelClaw extends Command {

    private final PanelClarm panelClarm;

    public OpenPanelClaw(PanelClarm panelClarm){
        this.panelClarm = panelClarm;
        requires(panelClarm);
    }

    @Override
    protected void execute() {
        this.panelClarm.OpenPanelClaw();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

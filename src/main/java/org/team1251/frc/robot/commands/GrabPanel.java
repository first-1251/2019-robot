package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team1251.frc.robot.subsystems.PanelClarm;
import org.team1251.frc.robotCore.commands.TimedNothing;

public class GrabPanel extends CommandGroup {

    private PanelClarm panelClarm;
    private TimedNothing timedNothing;

    public GrabPanel(PanelClarm panelClarm){
        addSequential(new ExtendPanelArm(panelClarm));
        addSequential(new GrabPanel(panelClarm));
        addParallel(new TimedNothing(1));
        addSequential(new RetractPanelArm(panelClarm));
    }
}

package org.team1251.frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team1251.frc.robot.subsystems.PanelClarm;
import org.team1251.frc.robotCore.commands.TimedNothing;

public class GrabPanel extends CommandGroup {

    public GrabPanel(PanelClarm panelClarm){
        addSequential(new ExtendPanelArm(panelClarm));
        addSequential(new TimedNothing(0.5));
        addSequential(new GrabPanel(panelClarm));
        addParallel(new TimedNothing(.10));
        addSequential(new RetractPanelArm(panelClarm));
    }
}

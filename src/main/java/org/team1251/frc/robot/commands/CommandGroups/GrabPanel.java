package org.team1251.frc.robot.commands.CommandGroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.team1251.frc.robot.commands.ExtendPanelArm;
import org.team1251.frc.robot.commands.OpenPanelClaw;
import org.team1251.frc.robot.commands.RetractPanelArm;
import org.team1251.frc.robot.subsystems.PanelClarm;
import org.team1251.frc.robotCore.commands.TimedNothing;

public class GrabPanel extends CommandGroup {

    public GrabPanel(PanelClarm panelClarm){
        addSequential(new ExtendPanelArm(panelClarm));
        addSequential(new TimedNothing(0.5));
        addSequential(new OpenPanelClaw(panelClarm));
        addParallel(new TimedNothing(.10));
        addSequential(new RetractPanelArm(panelClarm));
    }
}

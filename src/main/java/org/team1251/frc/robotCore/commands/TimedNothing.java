package org.team1251.frc.robotCore.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * A command which does nothing for a period of time. Useful for building delays into
 * CommandGroup implementations.
 */
public class TimedNothing extends TimedCommand {
    public TimedNothing(double timeout) {
        super(timeout);
    }
}

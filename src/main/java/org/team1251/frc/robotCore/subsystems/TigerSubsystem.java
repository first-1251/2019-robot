package org.team1251.frc.robotCore.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A subsystem that follows the patterns used by Team 1251, Tech Tigers.
 *
 * ### No internally defined default command
 *
 *   This class does not force you to implement `initDefaultCommand()` because:
 *     - It is tedious to implement an empty `initDefaultCommand()` for subsystems that don't have a default command.
 *     - `initDefaultCommand()` pretty much forces static access of commands and we've found that prone to hard-to-find
 *       bugs related to initialization order of static fields.
 *
 *   A default command can still be applied using `setDefaultCommand()` after the subsystem has been created.
 *   (see {@link edu.wpi.first.wpilibj.command.Subsystem#setDefaultCommand(Command)})
 */
public abstract class TigerSubsystem extends Subsystem {

    public TigerSubsystem() {
        super();
    }

    @Override
    protected void initDefaultCommand() { /* do nothing */ }
}

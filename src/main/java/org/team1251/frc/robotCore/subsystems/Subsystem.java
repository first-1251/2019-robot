package org.team1251.frc.robotCore.subsystems;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A subsystem that does not have an internally defined default command.
 *
 * A default command can still be applied using {@link edu.wpi.first.wpilibj.command.Subsystem#setDefaultCommand(Command)}.
 * This supports the Team 1251 pattern of using dependency injection. Using this class is simply a convenience over
 * the boilerplate operation of creating an empty `initDefaultCommand()` every time.
 */
public class Subsystem extends edu.wpi.first.wpilibj.command.Subsystem {

    @Override
    protected void initDefaultCommand() { /* do nothing */ }
}

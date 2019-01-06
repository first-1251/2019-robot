package org.team1251.frc.robotCore.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A subsystem that does not have a default command.
 *
 * This removes the redundancy of providing an empty `initDefaultCommand()` method.  This is purely for convenience.
 *
 * The `initDefaultCommand()` works well with the common pattern of making all commands globally statically accessible;
 * it solves the chicken/egg problem which prevents constructor injection (default command must require the subsystem
 * but the subsystem does not yet exist while it is being constructed!).
 *
 * Team 1251 solves this problem differently by simply making an additional call to`Subsystem.setDefaultCommand()` after
 * both the subsystem and the command have been constructed. This closely follows the "Setter Injection" pattern of
 * object oriented design (https://en.wikipedia.org/wiki/Dependency_injection#Setter_injection). Using this approach
 * eliminates the value behind `Subsystem.initDefaultCommand()`.
 *
 * TODO: Is there a (meaningful) performance-cost to introducing another abstraction layer into the class hierarchy?
 */
public class NoInitDefaultCmdSubsystem extends Subsystem {

    @Override
    protected void initDefaultCommand() { /* do nothing */ }
}

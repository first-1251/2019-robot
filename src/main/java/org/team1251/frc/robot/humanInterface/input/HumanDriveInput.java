package org.team1251.frc.robot.humanInterface.input;

import org.team1251.frc.robot.DrivePower;

/**
 * Interface for classes which are able to provide drive power based on human input.
 *
 * This interface makes it easier to swap out control schemes if we decide to.
 */
interface HumanDriveInput {
    DrivePower getDrivePower(HumanInput humanInput);
}

package org.team1251.frc.robot.robotMap;

public enum PcmDevice {

    DSOL_PANEL_ARM_FWD(0, 0), // Double Solenoid
    DSOL_PANEL_ARM_REV(0, 1), // Double Solenoid

    DSOL_PANEL_GRAPPLER_FWD(0, 2), // Double Solenoid
    DSOL_PANEL_GRAPPLER_REV(0, 3), // Double Solenoid

    DSOL_CLIMB_FRONT_LEG_ENGAGER_FWD(0, 6), // Double Solenoid
    DSOL_CLIMB_FRONT_LEG_ENGAGER_REV(0, 7), // Double Solenoid

    DSOL_CLIMB_REAR_LEG_ENGAGER_FWD(0, 4), // Double Solenoid
    DSOL_CLIMB_REAR_LEG_ENGAGER_REV(0, 5); // Double Solenoid

    public final int channel;
    public final int module;

    PcmDevice(int channel, int module) {
        this.channel = channel;
        this.module = module;
    }
}

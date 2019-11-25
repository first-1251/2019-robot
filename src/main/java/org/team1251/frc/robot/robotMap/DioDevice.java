package org.team1251.frc.robot.robotMap;

public enum DioDevice {

    //Limit Switch
    LS_FRONT_LIFT_LEG_LOWER(1), // Lever Switch
    LS_REAR_LIFT_LEG_LOWER(7); // Lever Switch

    public final int channel;

    DioDevice(int channel) {
        this.channel = channel;
    }
}


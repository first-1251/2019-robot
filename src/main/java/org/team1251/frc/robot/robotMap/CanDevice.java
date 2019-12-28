package org.team1251.frc.robot.robotMap;

public enum CanDevice {
    // PDP (MUST be 0)
    PDP(0),

    // Left drive train.
    MC_DRIVE_LEFT_TOP(1), // Talon
    MC_DRIVE_LEFT_FRONT(2), // Victor
    MC_DRIVE_LEFT_REAR(3), // Victor

    // Right Drive Train
    MC_DRIVE_RIGHT_TOP(4), // Talon
    MC_DRIVE_RIGHT_FRONT(5), // Victor
    MC_DRIVE_RIGHT_REAR(6), // Victor

    MC_CLIMB_ELEVATOR_FRONT(21), // Talon
    MC_CLIMB_LIFTER_REAR(22), // Talon

    MC_CLIMB_DRIVE(23), // Victor

    MC_FOOTBALL_EJECTOR_LEFT(13),
    MC_FOOTBALL_EJECTOR_RIGHT(11);

    public final int deviceNum;

    CanDevice(int deviceNum) {
        this.deviceNum = deviceNum;
    }
}

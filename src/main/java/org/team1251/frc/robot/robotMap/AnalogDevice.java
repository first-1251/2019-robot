package org.team1251.frc.robot.robotMap;

public enum AnalogDevice {
    IR_CLIMB_GROUND_SENSOR_FRONT(1), // Sharp IR
    IR_CLIMB_GROUND_SENSOR_REAR(0); //Sharp IR

    public final int channel;

    AnalogDevice(int channel) {
        this.channel = channel;
    }
}

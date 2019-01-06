package org.team1251.frc.robotCore.robotMap;

public enum PortType {
    PWM(9),
    DIO(9),
    PCM(7),
    ANALOG(3),
    CAN(99 /* Best guess */);

    public final int maxPort;

    PortType(int maxPort) {
        this.maxPort = maxPort;
    }
}
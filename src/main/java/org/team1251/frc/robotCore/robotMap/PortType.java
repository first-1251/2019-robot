package org.team1251.frc.robotCore.robotMap;

public enum PortType {
    PWM(9),
    DIO(19),
    PCM(13), // TODO: Drop back down to 7, for 2019!
    ANALOG(3),
    CAN(99 /* Best guess */);

    public final int maxPort;

    PortType(int maxPort) {
        this.maxPort = maxPort;
    }
}
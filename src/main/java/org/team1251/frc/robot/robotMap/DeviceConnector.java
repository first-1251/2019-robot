package org.team1251.frc.robot.robotMap;

import org.team1251.frc.robotCore.robotMap.DeviceConnectorInterface;
import org.team1251.frc.robotCore.robotMap.Port;
import org.team1251.frc.robotCore.robotMap.PortType;


/**
 * List all devices (sensors, actuators, motor controllers, etc) in this enum.
 */
public enum DeviceConnector implements DeviceConnectorInterface {

    /**
     * CAN port 0 is reserved for the PDP.
     *
     * From the docs (https://wpilib.screenstepslive.com/s/currentCS/m/java/l/219414-power-distribution-panel):
     *
     *    >"To work with the current versions of C++ and Java WPILib, the CAN ID for the PDP must be 0."
     */
    PDP_CAN(PortType.CAN, 0),

    MC_DRIVE_LEFT_BOTTOM(PortType.CAN, 1), // Talon
    MC_DRIVE_LEFT_TOP_FRONT(PortType.CAN, 2), // Victor
    MC_DRIVE_LEFT_TOP_REAR(PortType.CAN, 3), // Victor

    MC_DRIVE_RIGHT_BOTTOM(PortType.CAN, 4), // Talon
    MC_DRIVE_RIGHT_TOP_FRONT(PortType.CAN, 5), // Victor
    MC_DRIVE_RIGHT_TOP_REAR(PortType.CAN, 6), // Victor

    MC_ELEVATOR_GEARBOX_FRONT(PortType.CAN, 7), //Talon
    MC_ELEVATOR_GEARBOX_REAR(PortType.CAN, 8), //Talon

    MC_CARGO_ARM(PortType.CAN, 9), // Victor
    MC_CARGO_CLAW(PortType.CAN, 10), // Victor
    MC_CLIMB_ELEVATOR_FWD(PortType.CAN, 11), // Victor

    SOL_PANEL_CLAW(PortType.PCM, 0), // Single Solenoid
    DSOL_FORWARD_PANEL_ARM(PortType.PCM, 1), // Double Solenoid
    DSOL_BACKWARD_PANEL_ARM(PortType.PCM, 2), // Double Solenoid

    DSOL_PANEL_ELEV_SHIFT_FORWARD(PortType.PCM, 3), // Double Solenoid
    DSOL_PANEL_ELEV_SHIFT_BACKWARD(PortType.PCM, 4), // Double Solenoid

    DSOL_CARGO_ELEV_SHIFT_FORWARD(PortType.PCM,5), // Double Solenoid
    DSOL_CARGO_ELEV_SHIFT_BACKWARD(PortType.PCM,6), // Double Solenoid

    DSOL_FRONT_CLIMB_ELEV_SHIFT_FORWARD(PortType.PCM, 7), // Double Solenoid
    DSOL_FRONT_CLIMB_ELEV_SHIFT_BACKWARD(PortType.PCM, 8), // Double Solenoid

    DSOL_REAR_CLIMB_ELEV_SHIFT_FORWARD(PortType.PCM, 9), // Double Solenoid
    DSOL_REAR_CLIMB_ELEV_SHIFT_BACKWARD(PortType.PCM, 10), // Double Solenoid

    ENC_A_CLIMB_FRONT(PortType.DIO, 0), //Encoder
    ENC_B_CLIMB_FRONT(PortType.DIO, 1), //Encoder

    ENC_A_CLIMB_REAR(PortType.DIO, 2), //Encoder
    ENC_B_CLIMB_REAR(PortType.DIO, 3), //Encoder

    LS_CARGO_CLAW_ARM_UP(PortType.DIO, 4), //Limit Switch
    LS_CARGO_CLAW_ARM_DOWN(PortType.DIO, 5), //Limit Switch
    LS_CARGO_CLAW_ARM_COLLECT(PortType.DIO, 6), //Limit Switch

    LS_PATCH_CLAW_ARM_UP(PortType.DIO, 7), //Limit Switch
    LS_PATCH_CLAW_ARM_DOWN(PortType.DIO, 8), //Limit Switch

    LS_CARGO_ELEV_UP(PortType.DIO, 9), //Limit Switch
    LS_CARGO_ELEV_DOWN(PortType.DIO, 10), //Limit Switch

    LS_LEV_3_CLIMB_DOWN(PortType.DIO, 11), //Limit Switch
    LS_LEV_3_CLIMB_BOTH(PortType.DIO, 12), //Limit Switch

    SONAR_CLIMB_DETECTOR_FRONT(PortType.ANALOG, 0), //Sonar
    SONAR_CLIMB_DETECTOR_REAR(PortType.ANALOG, 1); //Sonar




    /**
     * Port assignment for each Device
     */
    private final Port port;

    /**
     * @param portType The port type that the device is attached to
     * @param port The port the device is attached to.
     */
    DeviceConnector(PortType portType, int port) {
        this.port = new Port(portType, port);
    }

    @Override
    public Port getPort() {
        return port;
    }
}
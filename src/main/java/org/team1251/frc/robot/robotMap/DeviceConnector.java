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

    //Motors
    MC_DRIVE_LEFT_TOP(PortType.CAN, 1), // Talon
    MC_DRIVE_LEFT_BOTTOM_FRONT(PortType.CAN, 2), // Victor
    MC_DRIVE_LEFT_BOTTOM_REAR(PortType.CAN, 3), // Victor

    MC_DRIVE_RIGHT_TOP(PortType.CAN, 4), // Talon
    MC_DRIVE_RIGHT_BOTTOM_FRONT(PortType.CAN, 5), // Victor
    MC_DRIVE_RIGHT_BOTTOM_REAR(PortType.CAN, 6), // Victor

    MC_MANIPULATOR_ELEVATOR(PortType.CAN, 11), //Talon
    MC_CARGO_ARM(PortType.CAN, 12), // Talon
    MC_CARGO_COLLECTOR(PortType.CAN, 13), // Talon

    MC_CLIMB_ELEVATOR_FRONT(PortType.CAN, 21), // Talon
    MC_CLIMB_ELEVATOR_REAR(PortType.CAN, 22), // Talon

    MC_CLIMB_DRIVE(PortType.CAN, 23), // Victor

    //Solenoids
    DSOL_PANEL_ARM_FWD(PortType.PCM, 0), // Double Solenoid
    DSOL_PANEL_ARM_REV(PortType.PCM, 1), // Double Solenoid

    DSOL_PANEL_GRAPPLER_FWD(PortType.PCM, 2), // Double Solenoid
    DSOL_PANEL_GRAPPLER_REV(PortType.PCM, 3), // Double Solenoid

    DSOL_CLIMB_ELEV_FRONT_SHIFTER_FWD(PortType.PCM, 4), // Double Solenoid
    DSOL_CLIMB_ELEV_FRONT_SHIFTER_REV(PortType.PCM, 5), // Double Solenoid

    DSOL_CLIMB_ELEV_REAR_SHIFTER_FWD(PortType.PCM, 6), // Double Solenoid
    DSOL_CLIMB_ELEV_REAR_SHIFTER_REV(PortType.PCM, 7), // Double Solenoid

    //Limit Switch
    LS_CLIMB_ELEV_FRONT_UPPER(PortType.DIO, 0), // Lever Switch
    LS_CLIMB_ELEV_FRONT_LOWER(PortType.DIO, 1), // Lever Switch

    LS_CLIMB_ELEV_REAR_UPPER(PortType.DIO, 6), // Lever Switch
    LS_CLIMB_ELEV_REAR_LOWER(PortType.DIO, 7), // Lever Switch

    LS_MANIPULATER_ELEVATOR_TOP(PortType.DIO, 2), // Lever Switch
    LS_MANIPULATER_ELEVATOR_BOTTOM(PortType.DIO, 3), // Lever Switch

    LS_CARGO_ARM_TOP(PortType.DIO, 4), // Lever Switch
    
    LS_CARGO_COLLECT_UPPER(PortType.DIO, 5), // Lever Switch
    LS_CARGO_COLLECT_LOWER(PortType.DIO, 8), // Lever Switch

    LS_PANEL_COLLECT(PortType.DIO, 9), // Lever Switch

    IR_CLIMB_GROUND_SENSOR_FRONT(PortType.ANALOG, 1), // Sharp IR
    IR_CLIMB_GROUND_SENSOR_REAR(PortType.ANALOG, 0); //Sharp IR


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
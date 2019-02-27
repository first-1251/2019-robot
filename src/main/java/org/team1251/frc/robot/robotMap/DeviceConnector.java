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

    MC_CLIMB_ELEVATOR_GEARBOX_FRONT(PortType.CAN, 21), //Talon
    MC_CLIMB_ELEVATOR_GEARBOX_REAR(PortType.CAN, 22), //Talon

    MANIPULATOR_MC_ELEVATOR_GEARBOX(PortType.CAN, 11), //Talon
    //TODO Remove Line Below Soon.
    //MANIPULATOR_MC_ELEVATOR_GEARBOX_REAR(PortType.CAN, 13), //Talon

    MC_CARGO_ARM(PortType.CAN, 12), // Victor
    MC_CARGO_CLAW(PortType.CAN, 13), // Victor
    MC_CLIMB_ELEVATOR_FWD(PortType.CAN, 23), // Victor

    //Solenoids
    DSOL_FORWARD_PANEL_ARM(PortType.PCM, 0), // Double Solenoid
    DSOL_REVERSE_PANEL_ARM(PortType.PCM, 1), // Double Solenoid

    DSOL_FORWARD_PANEL_CLAW(PortType.PCM, 2), // Double Solenoid
    DSOL_REVERSE_PANEL_CLAW(PortType.PCM, 3), // Double Solenoid

    //TODO I forgot the manipulator doesnt need a shifter now cause its only one lol.
    //DSOL_MANIPULATOR_ELEV_SHIFT_ENABLE(PortType.PCM, 3), // Double Solenoid
    //DSOL_MANIPULATOR_ELEV_SHIFT_DISABLE(PortType.PCM, 4), // Double Solenoid

    DSOL_FRONT_CLIMB_ELEV_SHIFT_ENABLE(PortType.PCM, 4), // Double Solenoid
    DSOL_FRONT_CLIMB_ELEV_SHIFT_DISABLE(PortType.PCM, 5), // Double Solenoid

    DSOL_REAR_CLIMB_ELEV_SHIFT_ENABLE(PortType.PCM, 6), // Double Solenoid
    DSOL_REAR_CLIMB_ELEV_SHIFT_DISABLE(PortType.PCM, 7), // Double Solenoid

    //Encoders
    ENC_A_CLIMB_FRONT(PortType.DIO, 77), //Encoder
    ENC_B_CLIMB_FRONT(PortType.DIO, 77), //Encoder

    ENC_A_CLIMB_REAR(PortType.DIO, 77), //Encoder
    ENC_B_CLIMB_REAR(PortType.DIO, 77), //Encoder

    //Limit Switch
    LS_REAR_CLIMB_UP(PortType.DIO, 0), //Limit Switch
    LS_REAR_CLIMB_DOWN(PortType.DIO, 1), //Limit Switch

    LS_MANIPULATER_ELEVATOR_UP(PortType.DIO, 2), //Limit Switch
    LS_MANIPULATER_ELEVATOR_DOWN(PortType.DIO, 3), //Limit Switch

    LS_CARGO_CLAW_ARM_UP(PortType.DIO, 4), //Limit Switch
    LS_CARGO_CLAW_ARM_DOWN(PortType.DIO, 5), //Limit Switch

    LS_FRONT_CLIMB_UP(PortType.DIO, 6), //Limit Switch
    LS_FRONT_CLIMB_DOWN(PortType.DIO, 7), //Limit Switch

    BS_CARGO_CLAW_ARM_COLLECT(PortType.DIO, 8), //Bumper Switch
    BS_PATCH_CLAW_COLLECT(PortType.DIO, 9), //Bumper Switch

    SONAR_CLIMB_DETECTOR_FRONT(PortType.ANALOG, 1), //Sonar
    SONAR_CLIMB_DETECTOR_REAR(PortType.ANALOG, 0); //Sonar




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
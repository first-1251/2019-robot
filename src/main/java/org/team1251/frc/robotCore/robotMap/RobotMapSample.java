package org.team1251.frc.robotCore.robotMap;

/**
 * A variation of the traditional RobotMap that accomplishes the same core purpose with a little more intelligence
 * build into it.
 *
 * The traditional RobotMap is documented as such:
 *
 *   The RobotMap is a mapping from the ports sensors and actuators are wired into
 *   to a variable name. This provides flexibility changing wiring, makes checking
 *   the wiring easier and significantly reduces the number of magic numbers
 *   floating around.
 *
 * The goals are sound. Magic numbers floating around the code base makes it difficult to make changes. Centralizing
 * them in the RobotMap simplifies it greatly.  However, we (Team 1251) still encountered some recurring challenges
 * around port assignments -- specifically, related to port collisions.
 *
 * The challenge is that port numbers repeat across port types. For example, PWM port `0` may be used for a drive train
 * motor while PCM port `0` may be used for the claw solenoid. The repeat of port numbers can make it difficult to
 * visually detect duplicates. Organizing RobotMap by port type can help, but is less intuitive than organizing it
 * by related devices (i.e. the arm motor being grouped with the arm potentiometer).
 *
 * When a port collision occurred, we found ourselves hunting-and-pecking around the RobotMap looking for the duplicate
 * assignment. In some cases, we found it; in others, we discovered that we just initialized two devices with the same
 * RobotMap constant... or initialized the same device twice (OOPS!).
 *
 * This RobotMap implementation uses the AssignmentManager to provide early detection of duplicate assignments with
 * meaningful error messages that identify the two devices involved in the collision.
 *
 * At the time of use, the device manager can be used to make sure that the assignment matches the expected type. For
 * example, when setting up a digital switch:
 *
 *      new DigitalSwitch(RobotMap.deviceManager.getPort(RobotMap.Devices.ELEVATOR_SWITCH_TOP, PortType.DIO))
 *
 * This is definitely more verbose than the classic RobotMap usage, but it has some distinct advantages:
 *
 *      1. When reviewing the initialization code, it is obvious that you are retrieving a digital input port. This is
 *         especially useful for less experienced students.
 *      2. If the design changes to use a different port type (for example moving to a CAN device), updating the
 *         classic RobotMap without updating the initialization may not create a failure -- your device simply won't
 *         respond.  Under this pattern, you will get a meaningful error when the device initializes.
 *
 */
public class RobotMapSample
{
    // The Devices enum is declared. This does not need to be declared within your RobotMap. It could just as easy
    // be in its own class file. Some teams may be more comfortable having it in the RobotMap due to familiarity.
    public enum DeviceAssignment implements PortAssignable {
        ELEVATOR_MOTOR(PortType.PWM, 0),
        ELEVATOR_SWITCH_TOP(PortType.DIO, 0);

        private final Assignment assignment;

        DeviceAssignment(PortType portType, int port) {
            this.assignment = new Assignment(portType, port);
        }

        @Override
        public Assignment getAssignment() {
            return assignment;
        }
    }

    // The device manager is passed simply passed the Enum class.
    public static final DeviceManager<DeviceAssignment> deviceManager = new DeviceManager<>(DeviceAssignment.class);

    // Duplication detection must be explicitly invoked. Doing it as a static initializer makes sure it is done
    // as soon as the RobotMap is loaded.
    static {
        deviceManager.detectDuplicates();
    }
}
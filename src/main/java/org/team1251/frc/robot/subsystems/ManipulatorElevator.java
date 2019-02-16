package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class ManipulatorElevator extends Subsystem {
    /**
     * PRE MESSAGE TO ALL THOSE READING
     * WHY ARE ALL 4 ELEVATORS ON ONE GEARBOX, THIS IS VERY VERY STUPID DESIGN
     * THIS SUBSYSTEM IS GOING TO BE TERRIBLE
     * PRAISE Nobuaki Katayama || JTHBD192620052807
     **/

    //Just Incase Motors are Inverted
    private static final boolean isManipulatorElevatorGearboxInvertedFront = false;
    private static final boolean isManipulatorElevatorGearboxInvertedRear = false;

    //Gearbox Speed Changes
    public static final double MANIPULATOR_ELEVATOR_SPEED = 0.5;


    private final DeviceManager deviceManager = Robot.deviceManager;

    //Speed Controller Initialization
    private SpeedController manipulatorElevatorGearboxFront;
    private SpeedController manipulatorElevatorGearboxRear;

    //Solenoid Initialization
    private DoubleSolenoid panelElevatorSolenoid;
    private DoubleSolenoid cargoElevatorSolenoid;

    public ManipulatorElevator(){

        manipulatorElevatorGearboxFront = deviceManager.createTalonSRX(DeviceConnector.MANIPULATOR_MC_ELEVATOR_GEARBOX_FRONT);
        manipulatorElevatorGearboxRear = deviceManager.createTalonSRX(DeviceConnector.MANIPULATOR_MC_ELEVATOR_GEARBOX_REAR);

        panelElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_ELEV_SHIFT_FORWARD, DeviceConnector.DSOL_PANEL_ELEV_SHIFT_BACKWARD);
        cargoElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_CARGO_ELEV_SHIFT_FORWARD, DeviceConnector.DSOL_CARGO_ELEV_SHIFT_BACKWARD);

       manipulatorElevatorGearboxFront.setInverted(isManipulatorElevatorGearboxInvertedFront);
       manipulatorElevatorGearboxRear.setInverted(isManipulatorElevatorGearboxInvertedRear);
    }


}

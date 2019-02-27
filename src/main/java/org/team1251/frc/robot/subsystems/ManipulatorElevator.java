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
    private DoubleSolenoid manipulatorElevatorSolenoid;

    public ManipulatorElevator(){

        manipulatorElevatorGearboxFront = deviceManager.createTalonSRX(DeviceConnector.MANIPULATOR_MC_ELEVATOR_GEARBOX_FRONT);
        manipulatorElevatorGearboxRear = deviceManager.createTalonSRX(DeviceConnector.MANIPULATOR_MC_ELEVATOR_GEARBOX_REAR);

        manipulatorElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_MANIPULATOR_ELEV_SHIFT_ENABLE, DeviceConnector.DSOL_MANIPULATOR_ELEV_SHIFT_DISABLE);

        manipulatorElevatorGearboxFront.setInverted(isManipulatorElevatorGearboxInvertedFront);
        manipulatorElevatorGearboxRear.setInverted(isManipulatorElevatorGearboxInvertedRear);
    }

    public void MoveManipulatorElevatorUp(){
        manipulatorElevatorGearboxFront.set(MANIPULATOR_ELEVATOR_SPEED);
        manipulatorElevatorGearboxRear.set(MANIPULATOR_ELEVATOR_SPEED);
    }

    public void MoveManipulatorElevatorDown(){
        manipulatorElevatorGearboxFront.set(-MANIPULATOR_ELEVATOR_SPEED);
        manipulatorElevatorGearboxFront.set(-MANIPULATOR_ELEVATOR_SPEED);
    }

    public void EnableManipulatorElevator(){
        manipulatorElevatorSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    public void DisableManipulatorElevator(){
        manipulatorElevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
}



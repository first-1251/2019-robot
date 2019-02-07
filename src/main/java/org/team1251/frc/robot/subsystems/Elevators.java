package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Elevators extends Subsystem {
    /**
     * PRE MESSAGE TO ALL THOSE READING
     * WHY ARE ALL 4 ELEVATORS ON ONE GEARBOX, THIS IS VERY VERY STUPID DESIGN
     * THIS SUBSYSTEM IS GOING TO BE TERRIBLE
     * PRAISE Nobuaki Katayama || JTHBD192620052807
     **/

    //Just Incase Motors are Inverted
    private static final boolean isClimbElevatorGearboxInverted = false;
    private static final boolean isFrontElevatorGearboxInverted = false;
    private static final boolean isRearElevatorGearboxInverted = false;

    //Gearbox Speed Changes
    public static final double CLIMB_ELEVATOR_SPEED = 0.5;
    public static final double CLIMB_GEARBOX_SPEED = 0.5;
    public static final double CARGO_ELEVATOR_SPEED = 0.5;
    public static final double PANEL_ELEVATOR_SPEED = 0.5;


    private final DeviceManager deviceManager = Robot.deviceManager;

    //Speed Controller Initialization
    private SpeedController frontElevatorGearbox;
    private SpeedController rearElevatorGearbox;
    private SpeedController climbElevatorGearbox;

    //Solenoid Initialization
    private DoubleSolenoid panelElevatorSolenoid;
    private DoubleSolenoid cargoElevatorSolenoid;
    private DoubleSolenoid frontClimbElevatorSolenoid;
    private DoubleSolenoid rearClimbElevatorSolenoid;

    public Elevators(){

        frontElevatorGearbox = deviceManager.createTalonSRX(DeviceConnector.MC_ELEVATOR_GEARBOX_FRONT);
        rearElevatorGearbox = deviceManager.createTalonSRX(DeviceConnector.MC_ELEVATOR_GEARBOX_REAR);
        climbElevatorGearbox = deviceManager.createVictorSPX(DeviceConnector.MC_CLIMB_ELEVATOR_FWD);

        panelElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_PANEL_ELEV_SHIFT_FORWARD, DeviceConnector.DSOL_PANEL_ELEV_SHIFT_BACKWARD);
        cargoElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_CARGO_ELEV_SHIFT_FORWARD, DeviceConnector.DSOL_CARGO_ELEV_SHIFT_BACKWARD);
        frontClimbElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_FRONT_CLIMB_ELEV_SHIFT_FORWARD, DeviceConnector.DSOL_FRONT_CLIMB_ELEV_SHIFT_BACKWARD);
        rearClimbElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_REAR_CLIMB_ELEV_SHIFT_FORWARD, DeviceConnector.DSOL_REAR_CLIMB_ELEV_SHIFT_BACKWARD);

        climbElevatorGearbox.setInverted(isClimbElevatorGearboxInverted);
        frontElevatorGearbox.setInverted(isFrontElevatorGearboxInverted);
        rearElevatorGearbox.setInverted(isRearElevatorGearboxInverted);
    }

    public void MoveClimbElevatorGearboxForward(){
        climbElevatorGearbox.set(CLIMB_GEARBOX_SPEED);
    }

    public void MoveClimbElevatorGearboxReverse(){
        climbElevatorGearbox.set(-CLIMB_GEARBOX_SPEED);
    }

    //Elevator Shifter Commands
    public void EnablePanelElevator(){
        panelElevatorSolenoid.set(DoubleSolenoid.Value.kForward);
        Robot.isClimbElevatorEnabled = true;

    }
    public void DisablePanelElevator(){
        panelElevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
        Robot.isClimbElevatorEnabled = false;

    }

}

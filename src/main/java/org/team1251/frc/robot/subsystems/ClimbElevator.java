package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class ClimbElevator extends Subsystem {

    /**
     * PRE MESSAGE TO ALL THOSE READING
     * THANK YOU FOR CHANGING THE STUPID DESIGN. 3 ELEVATORS AND 2 GEARBOXES
     * PRAISE Nobuaki Katayama || JTHBD192620052807
     **/

    //Just In case Motors are Inverted
    private static final boolean isClimbDriveGearboxInverted = false;
    private static final boolean isFrontClimbElevatorGearboxInverted = false;
    private static final boolean isRearClimbElevatorGearboxInverted = false;

    //Gearbox Speed
    public static final double FORWARD_CLIMB_GEARBOX_SPEED = 0.5;
    public static final double REVERSE_CLIMB_GEARBOX_SPEED = 0.5;
    public static final double CLIMB_ELEVATOR_SPEED = 0.5;

    private final DeviceManager deviceManager = Robot.deviceManager;

    //Speed Controller Initialization
    private SpeedController climbDriveGearbox;
    private SpeedController climbElevatorGearboxFront;
    private SpeedController climbElevatorGearboxRear;

    //Solenoid Initialization
    private DoubleSolenoid frontClimbElevatorSolenoid;
    private DoubleSolenoid rearClimbElevatorSolenoid;

    public ClimbElevator(){

        climbElevatorGearboxFront = deviceManager.createVictorSPX(DeviceConnector.MC_CLIMB_ELEVATOR_GEARBOX_FRONT);
        climbElevatorGearboxRear = deviceManager.createVictorSPX(DeviceConnector.MC_CLIMB_ELEVATOR_GEARBOX_REAR);
        climbDriveGearbox = deviceManager.createVictorSPX(DeviceConnector.MC_CLIMB_ELEVATOR_FWD);

        frontClimbElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_FRONT_CLIMB_ELEV_SHIFT_ENABLE, DeviceConnector.DSOL_FRONT_CLIMB_ELEV_SHIFT_DISABLE);
        rearClimbElevatorSolenoid = deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_REAR_CLIMB_ELEV_SHIFT_ENABLE, DeviceConnector.DSOL_REAR_CLIMB_ELEV_SHIFT_DISABLE);

        climbElevatorGearboxFront.setInverted(isFrontClimbElevatorGearboxInverted);
        climbElevatorGearboxRear.setInverted(isRearClimbElevatorGearboxInverted);
        climbDriveGearbox.setInverted(isClimbDriveGearboxInverted);


    }

    public void DriveClimbGearboxForward(){
        climbElevatorGearboxFront.set(FORWARD_CLIMB_GEARBOX_SPEED);
        climbElevatorGearboxRear.set(FORWARD_CLIMB_GEARBOX_SPEED);
    }

    public void DriveClimbGearboxReverse(){
        climbElevatorGearboxFront.set(-REVERSE_CLIMB_GEARBOX_SPEED);
        climbElevatorGearboxRear.set(-REVERSE_CLIMB_GEARBOX_SPEED);
    }

    public void MoveClimbElevatorUp(){
        climbElevatorGearboxFront.set(CLIMB_ELEVATOR_SPEED);
        climbElevatorGearboxRear.set(CLIMB_ELEVATOR_SPEED);
    }

    public void MoveClimbElevatorDown(){
        climbElevatorGearboxFront.set(-CLIMB_ELEVATOR_SPEED);
        climbElevatorGearboxRear.set(-CLIMB_ELEVATOR_SPEED);
    }

    public void EnableFrontClimbElevator(){
        frontClimbElevatorSolenoid.set(DoubleSolenoid.Value.kForward);
        frontClimbElevatorSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    public void DisableFrontClimbElevator(){
        frontClimbElevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
        frontClimbElevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void EnableRearClimbElevator(){
        rearClimbElevatorSolenoid.set(DoubleSolenoid.Value.kForward);
        rearClimbElevatorSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void DisableRearClimbElevator(){
        rearClimbElevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
        rearClimbElevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
}

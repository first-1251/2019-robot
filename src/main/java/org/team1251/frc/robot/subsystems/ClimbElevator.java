package org.team1251.frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class ClimbElevator extends Subsystem {

    //Just Incase Motors are Inverted
    private static final boolean isClimbElevatorGearboxInverted = false;
    private static final boolean isFrontElevatorGearboxInverted = false;
    private static final boolean isRearElevatorGearboxInverted = false;

    //Gearbox Speed Changes
    public static final double ELEVATOR_GEARBOX_SPEED = 0.5;
    public static final double FRONT_ELEVATOR_GEARBOX_SPEED = 0.1;
    public static final double REAR_ELEVATOR_GEARBOX_SPEED = 0.1;

    private final DeviceManager deviceManager = Robot.deviceManager;


    private SpeedController frontElevatorGearbox;
    private SpeedController rearElevatorGearbox;
    private SpeedController climbElevatorGearbox;

    public ClimbElevator(){

        frontElevatorGearbox = deviceManager.createTalonSRX(DeviceConnector.MC_ELEVATOR_GEARBOX_FRONT);
        rearElevatorGearbox = deviceManager.createTalonSRX(DeviceConnector.MC_ELEVATOR_GEARBOX_REAR);
        climbElevatorGearbox = deviceManager.createVictorSPX(DeviceConnector.MC_CLIMB_ELEVATOR_FWD);

        climbElevatorGearbox.setInverted(isClimbElevatorGearboxInverted);
        frontElevatorGearbox.setInverted(isFrontElevatorGearboxInverted);
        rearElevatorGearbox.setInverted(isRearElevatorGearboxInverted);
    }

    public void MoveClimbElevatorGearboxForward(){
        climbElevatorGearbox.set(ELEVATOR_GEARBOX_SPEED);
    }

    public void MoveClimbElevatorGearboxReverse(){
        climbElevatorGearbox.set(-ELEVATOR_GEARBOX_SPEED);
    }

}

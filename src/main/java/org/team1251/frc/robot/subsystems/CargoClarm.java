package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.SpeedController;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class CargoClarm extends Subsystem {

    private final static double CLAW_SPEED = 0.5;
    private final static double ARM_SPEED = 0.25;


    private final DeviceManager deviceManager = Robot.deviceManager;

    private SpeedController cargoArmMotor;
    private SpeedController cargoClawMotor;

    public CargoClarm(){

        cargoArmMotor = deviceManager.createVictorSPX(DeviceConnector.MC_CARGO_ARM);
        cargoClawMotor = deviceManager.createVictorSPX(DeviceConnector.MC_CARGO_CLAW);
    }

    public void MoveCargoArmUp (){
        cargoArmMotor.set(-ARM_SPEED);
    }

    public void MoveCargoArmDown (){
        cargoArmMotor.set(ARM_SPEED);
    }

    public void OutakeCargo () {
        cargoClawMotor.set(CLAW_SPEED);
    }

    public void IntakeCargo () {
        cargoClawMotor.set(-CLAW_SPEED);
    }


}

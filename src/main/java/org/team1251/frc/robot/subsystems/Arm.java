package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.feedback.NormallyOpenSwitch;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Arm extends Subsystem {

    private final static boolean ARM_MOTOR_INVERTED = false;
    private final static double ARM_SPEED = .75;

    private final DeviceManager deviceManager = Robot.deviceManager;
    private final WPI_TalonSRX armMotorController;


    private final NormallyOpenSwitch armUpperLimitSwitch;
    private final NormallyOpenSwitch armLowerLimitSwitch;


    public Arm() {
        armMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_CARGO_ARM);
        armMotorController.setNeutralMode(NeutralMode.Brake);
        armMotorController.setInverted(ARM_MOTOR_INVERTED);

        armUpperLimitSwitch = deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_CARGO_ARM_TOP);
        armLowerLimitSwitch = deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_CARGO_ARM_BOTTOM);
    }

    public void stopArmMotor() {
        armMotorController.set(0);
    }

    public void moveCargoArmUp() {
        if (isArmUp()) {
            armMotorController.set(0);
        } else {
            armMotorController.set(-ARM_SPEED);
        }
    }

    public void moveCargoArmDown() {
        if (isArmDown()) {
            armMotorController.set(0);
        } else {
            armMotorController.set(ARM_SPEED);
        }
    }

    public boolean isArmUp() {
        return armUpperLimitSwitch.isActive();
    }

    public boolean isArmDown() {
        return armLowerLimitSwitch.isActive();
    }
}

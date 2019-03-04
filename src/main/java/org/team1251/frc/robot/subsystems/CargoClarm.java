package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class CargoClarm extends Subsystem {

    private final static double COLLECTOR_MOTOR_SPEED = 0.75;
    private final static double ARM_SPEED = .75;

    private final static boolean ARM_MOTOR_INVERTED = false;
    private final static boolean COLLECTOR_MOTOR_INVERTED = false;

    private final static boolean ARM_LIMIT_SWITCH_UPPER_ON_STATE = true;
    private final static boolean COLLECTOR_SWITCH_UPPER_ON_STATE = true;
    private final static boolean COLLECTOR_SWITCH_LOWER_ON_STATE = true;

    private final DeviceManager deviceManager = Robot.deviceManager;

    private final WPI_TalonSRX armMotorController;
    private final WPI_TalonSRX collectorMotorController;

    private final DigitalInput armUpperLimitSwitch;
    private final DigitalInput upperCollectionSwitch;
    private final DigitalInput lowerCollectionSwitch;

    public CargoClarm(){
        armMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_CARGO_ARM);
        armMotorController.setNeutralMode(NeutralMode.Brake);
        armMotorController.setInverted(ARM_MOTOR_INVERTED);

        armUpperLimitSwitch = deviceManager.createDigitalInput(DeviceConnector.LS_CARGO_ARM_TOP);

        collectorMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_CARGO_COLLECTOR);
        collectorMotorController.setNeutralMode(NeutralMode.Coast);
        collectorMotorController.setInverted(COLLECTOR_MOTOR_INVERTED);

        upperCollectionSwitch = deviceManager.createDigitalInput(DeviceConnector.LS_CARGO_COLLECT_UPPER);
        lowerCollectionSwitch = deviceManager.createDigitalInput(DeviceConnector.LS_CARGO_COLLECT_LOWER);
    }

    public void moveCargoArmUp() {
        if (isArmUp()) {
            armMotorController.set(0);
        } else {
            armMotorController.set(-ARM_SPEED);
        }
    }

    public void moveCargoArmDown() {
        armMotorController.set(ARM_SPEED);
    }

    public void collect(boolean isEnabled) {
        if (!isEnabled || isCargoCollected()) {
            collectorMotorController.set(0);
        } else {
            collectorMotorController.set(COLLECTOR_MOTOR_SPEED);
        }
    }

    public void eject(boolean isEnabled) {
        if (!isEnabled) {
            collectorMotorController.set(0);
        } else {
            collectorMotorController.set(-COLLECTOR_MOTOR_SPEED);
        }
    }

    public boolean isCargoCollected() {
        return upperCollectionSwitch.get() == COLLECTOR_SWITCH_UPPER_ON_STATE ||
               lowerCollectionSwitch.get() == COLLECTOR_SWITCH_LOWER_ON_STATE;
    }

    public boolean isArmUp() {
        return armUpperLimitSwitch.get() == ARM_LIMIT_SWITCH_UPPER_ON_STATE;
    }

}

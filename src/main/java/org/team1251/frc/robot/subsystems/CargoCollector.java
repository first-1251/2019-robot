package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.team1251.frc.robot.feedback.NormallyOpenSwitch;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robotCore.subsystems.Subsystem;

// TODO: Use the encoder as top/bottom redundancy!
public class CargoCollector extends Subsystem {

    private final static double COLLECTOR_MOTOR_SPEED = 0.75;
    private final static boolean COLLECTOR_MOTOR_INVERTED = false;



    private final WPI_TalonSRX collectorMotorController;
    private final NormallyOpenSwitch collectionSwitch;

    public CargoCollector() {

        collectorMotorController = getDeviceManager().createTalonSRX(DeviceConnector.MC_CARGO_COLLECTOR);
        collectorMotorController.setNeutralMode(NeutralMode.Coast);
        collectorMotorController.setInverted(COLLECTOR_MOTOR_INVERTED);

        collectionSwitch = getDeviceManager().createNormallyOpenSwitch(DeviceConnector.LS_CARGO_COLLECT);
    }

    public void stopCollector() {
        collectorMotorController.set(0);
    }

    public void collect() {
        if (isCargoCollected()) {
            stopCollector();
        } else {
            collectorMotorController.set(COLLECTOR_MOTOR_SPEED);
        }
    }

    public void eject() {
        collectorMotorController.set(-COLLECTOR_MOTOR_SPEED);
    }

    public boolean isCargoCollected() {
        return collectionSwitch.isActive();
    }

    @Override
    public void sendTelemetryData() {
        getSensorTable().getEntry("collectionSwitch").setBoolean(collectionSwitch.isActive());

        getStateTable().getEntry("isCargoCollected").setBoolean(isCargoCollected());
    }
}

package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTable;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.feedback.MagEncoder;
import org.team1251.frc.robot.feedback.NormallyOpenSwitch;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Arm extends Subsystem {

    private final static boolean ARM_MOTOR_INVERTED = false;
    private final static double ARM_SPEED = .75;

    private final WPI_TalonSRX armMotorController;


    private final NormallyOpenSwitch armUpperLimitSwitch;
    private final NormallyOpenSwitch armLowerLimitSwitch;
    private final MagEncoder encoder;


    public Arm() {
        armMotorController = getDeviceManager().createTalonSRX(DeviceConnector.MC_CARGO_ARM);

        armMotorController.configFactoryDefault(20);

        armMotorController.setNeutralMode(NeutralMode.Brake);
        armMotorController.setInverted(ARM_MOTOR_INVERTED);

        armMotorController.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2 ); // default 20

        armUpperLimitSwitch = getDeviceManager().createNormallyOpenSwitch(DeviceConnector.LS_CARGO_ARM_TOP);
        armLowerLimitSwitch = getDeviceManager().createNormallyOpenSwitch(DeviceConnector.LS_CARGO_ARM_BOTTOM);

        encoder = new MagEncoder(armMotorController, 1);
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

    public void testArmMotor (){
        armMotorController.set(0.25);
    }

    public void stopArmMotor () {
        armMotorController.set(0);
    }



    public boolean isArmUp() {
        return armUpperLimitSwitch.isActive();
    }

    public boolean isArmDown() {
        return armLowerLimitSwitch.isActive();
    }



    @Override
    public void sendTelemetryData() {
        NetworkTable sensorTable = getSensorTable();
        sensorTable.getEntry("upperLimitSwitch").setBoolean(armUpperLimitSwitch.isActive());
        sensorTable.getEntry("upperLimitSwitch").setBoolean(armLowerLimitSwitch.isActive());
        sensorTable.getEntry("encoderPosition").getDouble(encoder.getAbsolutePosition());

        NetworkTable stateTable = getStateTable();
        stateTable.getEntry("isArmUp").setBoolean(isArmUp());
        stateTable.getEntry("isArmDown").setBoolean(isArmDown());
    }
}

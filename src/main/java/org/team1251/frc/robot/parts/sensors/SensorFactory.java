package org.team1251.frc.robot.parts.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import org.team1251.frc.robot.robotMap.AnalogDevice;
import org.team1251.frc.robot.robotMap.DioDevice;

public class SensorFactory {

    private static final double DRIVE_TRAIN_ENCODER_REV_DISTANCE = 1;
    private static final double LIFT_LEG_ENCODER_REV_DISTANCE = 1.185;

    public MagEncoder createRightDriveTrainEncoder(TalonSRX attachedTalon) {
        return new MagEncoder(attachedTalon, DRIVE_TRAIN_ENCODER_REV_DISTANCE, false);
    }

    public MagEncoder createLeftDriveTrainEncoder(TalonSRX attachedTalon) {
        return new MagEncoder(attachedTalon, DRIVE_TRAIN_ENCODER_REV_DISTANCE, false);
    }

    public MagEncoder createFrontLiftLegEncoder(TalonSRX attachedTalon) {
        return new MagEncoder(attachedTalon, LIFT_LEG_ENCODER_REV_DISTANCE, false);
    }

    public MagEncoder createRearLiftLegEncoder(TalonSRX attachedTalon) {
        return new MagEncoder(attachedTalon, LIFT_LEG_ENCODER_REV_DISTANCE, false);
    }

    public NormallyOpenSwitch createFrontLiftLegLowerLimitSwitch() {
        return new NormallyOpenSwitch(DioDevice.LS_FRONT_LIFT_LEG_LOWER.channel);
    }

    public NormallyOpenSwitch createRearLiftLegLowerLimitSwitch() {
        return new NormallyOpenSwitch(DioDevice.LS_REAR_LIFT_LEG_LOWER.channel);
    }

    public GroundDetector createFrontGroundDetector() {
        return new GroundDetector(new AnalogInput(AnalogDevice.IR_CLIMB_GROUND_SENSOR_FRONT.channel));
    }

    public GroundDetector createRearGroundDetector() {
        return new GroundDetector(new AnalogInput(AnalogDevice.IR_CLIMB_GROUND_SENSOR_REAR.channel));
    }
}

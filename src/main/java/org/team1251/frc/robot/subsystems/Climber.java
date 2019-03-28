package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTable;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.actuators.LiftElevatorEngager;
import org.team1251.frc.robot.feedback.GroundDetector;
import org.team1251.frc.robot.feedback.MagEncoder;
import org.team1251.frc.robot.feedback.NormallyOpenSwitch;
import org.team1251.frc.robot.robotMap.DeviceConnector;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Climber extends Subsystem {

    /**
     * PRE MESSAGE TO ALL THOSE READING
     * THANK YOU FOR CHANGING THE STUPID DESIGN. 3 ELEVATORS AND 2 GEARBOXES
     * PRAISE Nobuaki Katayama || JTHBD192620052807
     **/

    //Just In case Motors are Inverted
    private static final boolean LIFT_MOTOR_FRONT_INVERTED = false;
    private static final boolean LIFT_MOTOR_REAR_INVERTED = false;
    private static final boolean DRIVE_MOTOR_INVERTED = false;

    private static final boolean LIFT_ENGAGER_FRONT_INVERTED = true;
    private static final boolean LIFT_ENGAGER_REAR_INVERTED = true;

    private static final boolean ELEVATOR_ENCODER_FRONT_PHASE_INVERSION = false;
    private static final boolean ELEVATOR_ENCODER_REAR_PHASE_INVERSION = false;

    private static final double DISTANCE_PER_ENCODER_REVOLUTION = 1.185; // 1 inch per revolution.
    private static final double RETRACTED_DISTANCE_THRESHOLD = .10; // Maximum travel distance to consider the elevator retracted.

    // Motor Speeds
    public static final double LIFT_SPEED = 1;
    public static final double LIFT_SUSTAIN_SPEED = .10; // .10 is a good at ~123 lbs
    public static final double SLOW_LIFT_SPEED = .5;

    private final DeviceManager deviceManager = Robot.deviceManager;

    private LiftElevatorEngager elevatorFrontEngager;
    private LiftElevatorEngager elevatorRearEngager;

    private WPI_TalonSRX driveMotorController;
    private WPI_TalonSRX liftMotorControllerFront;
    private WPI_TalonSRX liftMotorControllerRear;

    private NormallyOpenSwitch elevatorFrontUpperLimitSwitch; // on when fully retracted
    private NormallyOpenSwitch elevatorFrontLowerLimitSwitch; // on when fully extended

    private NormallyOpenSwitch elevatorRearUpperLimitSwitch; // on when fully retracted
    private NormallyOpenSwitch elevatorRearLowerLimitSwitch; // on when fully extended

    private MagEncoder elevatorRearEncoder;
    private GroundDetector groundDetectorRear;
    private GroundDetector groundDetectorFront;
    private double targetDistance = 1; // Safe default in case of bugs that don't set it properly.

    public MagEncoder getElevatorRearEncoder() {
        return elevatorRearEncoder;
    }

    public MagEncoder getElevatorFrontEncoder() {
        return elevatorFrontEncoder;
    }

    private MagEncoder elevatorFrontEncoder;

    public LiftElevatorEngager getElevatorFrontEngager() {
        return elevatorFrontEngager;
    }

    public LiftElevatorEngager getElevatorRearEngager() {
        return elevatorRearEngager;
    }

    public enum Target {
        HAB_LVL_2(8),
        HAB_LVL_3(21);

        public final double targetHeight;

        Target(double targetHeight) {
            this.targetHeight = targetHeight;
        }
    }


    public Climber(){

        establishLiftMotorControllers();
        establishElevatorEngagers();
        establishLiftLimitSwitches();
        establishElevatorEncoders();
        establishGroundDetectors();
    }

    private void establishGroundDetectors() {
        // Duplicate call protection.
        if (groundDetectorFront != null) {
            return;
        }

        groundDetectorFront = deviceManager.createGroundDetector(DeviceConnector.IR_CLIMB_GROUND_SENSOR_FRONT);
        groundDetectorRear = deviceManager.createGroundDetector(DeviceConnector.IR_CLIMB_GROUND_SENSOR_REAR);
    }

    private void establishElevatorEncoders() {
        // Duplicate call protection.
        if (elevatorFrontEncoder != null) {
            return;
        }

        elevatorFrontEncoder =
                new MagEncoder(liftMotorControllerFront, DISTANCE_PER_ENCODER_REVOLUTION, ELEVATOR_ENCODER_FRONT_PHASE_INVERSION);
        elevatorRearEncoder =
                new MagEncoder(liftMotorControllerRear, DISTANCE_PER_ENCODER_REVOLUTION, ELEVATOR_ENCODER_REAR_PHASE_INVERSION);

        // Assume both are at their zero position. (Fairly safe assumption, unless we started the robot
        // with the elevators extended!
        elevatorFrontEncoder.reset();
        elevatorRearEncoder.reset();
    }

    private void establishElevatorEngagers() {

        // Duplicate call protection.
        if (elevatorFrontEngager != null) {
            return;
        }

        elevatorFrontEngager = new LiftElevatorEngager(
                deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_CLIMB_ELEV_FRONT_SHIFTER_FWD, DeviceConnector.DSOL_CLIMB_ELEV_FRONT_SHIFTER_REV),
                LIFT_ENGAGER_FRONT_INVERTED
        );

        elevatorRearEngager = new LiftElevatorEngager(
                deviceManager.createDoubleSolenoid(DeviceConnector.DSOL_CLIMB_ELEV_REAR_SHIFTER_FWD, DeviceConnector.DSOL_CLIMB_ELEV_REAR_SHIFTER_REV),
                LIFT_ENGAGER_REAR_INVERTED
        );
    }

    private void establishLiftMotorControllers() {

        // Duplicate call protection.
        if (liftMotorControllerFront != null) {
            return;
        }

        liftMotorControllerFront = deviceManager.createTalonSRX(DeviceConnector.MC_CLIMB_ELEVATOR_FRONT);
        liftMotorControllerRear = deviceManager.createTalonSRX(DeviceConnector.MC_CLIMB_ELEVATOR_REAR);

        // Reset to defaults to avoid unexpected behavior from previous runs.
        liftMotorControllerFront.configFactoryDefault(20);
        liftMotorControllerRear.configFactoryDefault(20);

        liftMotorControllerFront.setInverted(LIFT_MOTOR_FRONT_INVERTED);
        liftMotorControllerFront.setNeutralMode(NeutralMode.Brake);

        liftMotorControllerRear.setInverted(LIFT_MOTOR_REAR_INVERTED);
        liftMotorControllerRear.setNeutralMode(NeutralMode.Brake);

        // Adjust the update rates of important things. Use half the robot period to maximize chances of getting an
        // update on every robot tick.
        liftMotorControllerFront.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2 ); // default 20
        liftMotorControllerRear.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2); // default 20

        // Decrease some things on the follower that aren't important in that context. This helps offset the bandwidth
        // cost of increasing important things.
        liftMotorControllerRear.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 100); // default 10
        liftMotorControllerRear.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100); // default 20

        // Explicitly set both motors to 0, as an attempted workaround to follower mode not working.
        liftMotorControllerFront.set(ControlMode.PercentOutput, 0);
        liftMotorControllerRear.set(ControlMode.PercentOutput, 0);

        driveMotorController = deviceManager.createTalonSRX(DeviceConnector.MC_CLIMB_DRIVE);
        liftMotorControllerFront.configFactoryDefault(20);
        driveMotorController.setInverted(DRIVE_MOTOR_INVERTED);
        driveMotorController.setNeutralMode(NeutralMode.Coast);
    }

    private void establishLiftLimitSwitches() {
        // Duplicate call protection.
        if (elevatorFrontLowerLimitSwitch != null) {
            return;
        }

        elevatorFrontLowerLimitSwitch =
                deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_CLIMB_ELEV_FRONT_LOWER);

        elevatorRearLowerLimitSwitch =
                deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_CLIMB_ELEV_REAR_LOWER);

        elevatorFrontUpperLimitSwitch =
                deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_CLIMB_ELEV_FRONT_UPPER);

        elevatorRearUpperLimitSwitch =
                deviceManager.createNormallyOpenSwitch(DeviceConnector.LS_CLIMB_ELEV_REAR_UPPER);

    }

    //Elevator Drive
    public void drive(double speed){
        driveMotorController.set(speed);
    }

    public boolean lift(Target target) {
        return lift(false, target.targetHeight);
    }

    public void relieveFrontPressure() {
        if (elevatorFrontEncoder.getDistance() > targetDistance - 1) {
            liftMotorControllerFront.set(-.25);
        }
    }

    /**
     *
     * @return true if fully lifted.
     */
    public boolean lift(boolean isSlow, double distance) {

        this.targetDistance = distance;

        // Make sure both elevators are engaged before lifting.
        elevatorFrontEngager.setState(true);
        elevatorRearEngager.setState(true);

        // Stop lifting as soon as either elevator reaches its upper limit. They do not move independently, so first
        // one there wins.
        if (isLifted()) {
            // Switch to sustain.
            sustain();
            return true;
        } else {
            liftMotorControllerFront.set(isSlow ? SLOW_LIFT_SPEED : LIFT_SPEED);
            liftMotorControllerRear.set(isSlow ? SLOW_LIFT_SPEED : LIFT_SPEED);
            return false;
        }
    }

    public boolean retractFront() {
        elevatorFrontEngager.setState(false); // Disengage and let the constant spring do its thing.
        return isFrontElevatorRetracted();
    }

    public boolean retractRear() {
        elevatorRearEngager.setState(false); // Disengage and let the constant spring do its thing.
        return isRearElevatorRetracted();
    }

    public boolean isFrontOnSolidGround() {
        return groundDetectorFront.isGroundDetected();
    }

    public boolean isRearOnSolidGround() {
        return groundDetectorRear.isGroundDetected();
    }

    public void sustain() {
        // TODO: position PID to sustain?
        liftMotorControllerFront.set(LIFT_SUSTAIN_SPEED);
        liftMotorControllerRear.set(LIFT_SUSTAIN_SPEED);
    }

    public void kill() {
        liftMotorControllerFront.set(0);
        liftMotorControllerRear.set(0);
    }

    public void testMotorControllerFront() {
        // Make sure the rear motor isn't running and run the front motor at low power.
        stopMotorControllerRear();
        liftMotorControllerFront.set(0.25);
    }

    public void testMotorControllerRear() {
        // Make sure the front motor isn't running and run the front motor at low power.
        stopMotorControllerFront();
        liftMotorControllerRear.set(0.25);
    }

    public void testDriveMotorController(){
        // Run the drive motor at low power.
        driveMotorController.set(0.25);
    }

    public void stopMotorControllerFront() {
        liftMotorControllerFront.set(0);
    }

    public void stopMotorControllerRear() {
        liftMotorControllerRear.set(0);
    }

    public void stopDriveMotorController(){
        driveMotorController.set(0);
    }


    public boolean isFrontElevatorRetracted() {
        return elevatorFrontUpperLimitSwitch.isActive() ||
                elevatorFrontEncoder.getDistance() <= RETRACTED_DISTANCE_THRESHOLD;
    }

    public boolean isRearElevatorRetracted() {
        return elevatorRearUpperLimitSwitch.isActive() || elevatorRearEncoder.getDistance() <= RETRACTED_DISTANCE_THRESHOLD;
    }

    public boolean isFrontLifted() {
        return elevatorFrontLowerLimitSwitch.isActive() || elevatorFrontEncoder.getDistance() >= targetDistance;
    }

    public boolean isRearLifted() {
        // Redundancy -- We'll accept either the switch or an threshold travel distance value from the encoder.
        return elevatorRearLowerLimitSwitch.isActive() || elevatorRearEncoder.getDistance() >= targetDistance;
    }

    public boolean isLifted() {
        return isFrontLifted() || isRearLifted();
    }

    public void printDiagnostics() {
        System.out.println("Front: " + elevatorFrontEncoder.getDistance() + "/" + elevatorFrontEncoder.getVelocity() + " | Rear:" + elevatorRearEncoder.getDistance() + "/" + elevatorRearEncoder.getVelocity());
    }

    @Override
    public void sendTelemetryData() {

        NetworkTable sensorTable = getSensorTable();
        sensorTable.getEntry("frontEncoderDistance").setDouble(elevatorFrontEncoder.getDistance());
        sensorTable.getEntry("frontEncoderVelocity").setDouble(elevatorFrontEncoder.getVelocity());
        sensorTable.getEntry("frontEncoderPosition").setDouble(elevatorFrontEncoder.getPosition());

        sensorTable.getEntry("rearEncoderDistance").setDouble(elevatorRearEncoder.getDistance());
        sensorTable.getEntry("rearEncoderVelocity").setDouble(elevatorRearEncoder.getVelocity());
        sensorTable.getEntry("rearEncoderPosition").setDouble(elevatorRearEncoder.getPosition());

        sensorTable.getEntry("frontLowerLimitSwitch").setBoolean(elevatorFrontLowerLimitSwitch.isActive());
        sensorTable.getEntry("frontLowerLimitSwitch").setBoolean(elevatorFrontLowerLimitSwitch.isActive());

        sensorTable.getEntry("rearUpperLimitSwitch").setBoolean(elevatorRearUpperLimitSwitch.isActive());
        sensorTable.getEntry("rearLowerLimitSwitch").setBoolean(elevatorRearLowerLimitSwitch.isActive());

        sensorTable.getEntry("frontGroundDetect").setBoolean(groundDetectorFront.isGroundDetected());
        sensorTable.getEntry("frontGroundDetectVoltage").setNumber(groundDetectorFront.getVoltage());

        sensorTable.getEntry("rearGroundDetect").setBoolean(groundDetectorRear.isGroundDetected());
        sensorTable.getEntry("rearGroundDetectVoltage").setNumber(groundDetectorRear.getVoltage());

        NetworkTable stateTable = getStateTable();
        stateTable.getEntry("isLifted").setBoolean(isLifted());
        stateTable.getEntry("height").setDouble(Math.max(elevatorFrontEncoder.getDistance(), elevatorRearEncoder.getDistance()));
        stateTable.getEntry("isFrontRetracted").setBoolean(isFrontElevatorRetracted());
        stateTable.getEntry("isRearRetracted").setBoolean(isFrontElevatorRetracted());
        stateTable.getEntry("isFrontOnSolidGround").setBoolean(isFrontOnSolidGround());
        stateTable.getEntry("isRearOnSolidGround").setBoolean(isFrontOnSolidGround());
    }
}

package org.team1251.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.feedback.GroundDetector;
import org.team1251.frc.robot.feedback.MagEncoder;
import org.team1251.frc.robot.actuators.LiftElevatorEngager;
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

    private static final boolean LIFT_ENGAGER_FRONT_INVERTED = false;
    private static final boolean LIFT_ENGAGER_REAR_INVERTED = false;

    private static final boolean ELEVATOR_ENCODER_FRONT_PHASE_INVERSION = false;
    private static final boolean ELEVATOR_ENCODER_REAR_PHASE_INVERSION = false;

    private static final double DISTANCE_PER_ENCODER_REVOLUTION = 1d; // 1 inch per revolution.
    private static final double RETRACTED_DISTANCE_THRESHOLD = .10; // Maximum travel distance to consider the elevator retracted.
    private static final double LIFTED_DISTANCE_THRESHOLD = 20.90; // Minimum travel distance to consider elevator lifted.

    // Motor Speeds
    public static final double LIFT_SPEED = 1;
    public static final double LIFT_SUSTAIN_SPEED = .50; // TODO: Tweak this.

    private final DeviceManager deviceManager = Robot.deviceManager;

    private LiftElevatorEngager elevatorFrontEngager;
    private LiftElevatorEngager elevatorRearEngager;

    //Speed Controller Initialization
    private WPI_TalonSRX liftControllerLead;
    private WPI_VictorSPX driveMotorController;


    private WPI_TalonSRX liftMotorControllerFront;
    private WPI_TalonSRX liftMotorControllerRear;

    private NormallyOpenSwitch elevatorFrontUpperLimitSwitch; // on when fully retracted
    private NormallyOpenSwitch elevatorFrontLowerLimitSwitch; // on when fully extended

    private NormallyOpenSwitch elevatorRearUpperLimitSwitch; // on when fully retracted
    private NormallyOpenSwitch elevatorRearLowerLimitSwitch; // on when fully extended

    private MagEncoder elevatorRearEncoder;
    private GroundDetector groundDetectorRear;
    private GroundDetector groundDetectorFront;

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
        liftMotorControllerFront.setNeutralMode(NeutralMode.Coast);

        liftMotorControllerRear.setInverted(LIFT_MOTOR_REAR_INVERTED);
        liftMotorControllerRear.setNeutralMode(NeutralMode.Coast);

        // Make the motor controller for the front lift motor the lead.
        liftControllerLead = liftMotorControllerFront;
        liftMotorControllerRear.follow(liftControllerLead);

        // Adjust the update rates of important things. Use half the robot period to maximize chances of getting an
        // update on every robot tick.
        liftMotorControllerFront.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2 ); // default 20
        liftMotorControllerRear.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Robot.TICK_PERIOD_MS / 2); // default 20

        // Decrease some things on the follower that aren't important in that context. This helps offset the bandwidth
        // cost of increasing important things.
        liftMotorControllerRear.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 100); // default 10
        liftMotorControllerRear.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100); // default 20


        driveMotorController = deviceManager.createVictorSPX(DeviceConnector.MC_CLIMB_ELEVATOR_FRONT);

        driveMotorController.setInverted(DRIVE_MOTOR_INVERTED);
        liftMotorControllerFront.setNeutralMode(NeutralMode.Coast);
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

    /**
     *
     * @return true if fully lifted.
     */
    public boolean lift() {
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
            liftControllerLead.set(LIFT_SPEED);
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
        liftControllerLead.set(LIFT_SUSTAIN_SPEED);
    }

    public boolean isFrontElevatorRetracted() {
        return elevatorFrontUpperLimitSwitch.isActive() ||
                elevatorFrontEncoder.getDistance() <= RETRACTED_DISTANCE_THRESHOLD;
    }

    public boolean isRearElevatorRetracted() {
        return elevatorRearUpperLimitSwitch.isActive() || elevatorRearEncoder.getDistance() <= RETRACTED_DISTANCE_THRESHOLD;
    }

    public boolean isFrontLifted() {
        return elevatorFrontLowerLimitSwitch.isActive() || elevatorFrontEncoder.getDistance() >= LIFTED_DISTANCE_THRESHOLD;
    }

    public boolean isRearLifted() {
        // Redundancy -- We'll accept either the switch or an threshold travel distance value from the encoder.
        return elevatorRearLowerLimitSwitch.isActive() || elevatorRearEncoder.getDistance() >= LIFTED_DISTANCE_THRESHOLD;
    }

    public boolean isLifted() {
        return isFrontLifted() || isRearLifted();
    }
}

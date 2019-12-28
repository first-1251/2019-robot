package org.team1251.frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.team1251.frc.robot.commands.AbandonClimb;
import org.team1251.frc.robot.commands.Climb;
import org.team1251.frc.robot.commands.TeleopDrive;
import org.team1251.frc.robot.commands.football.LiftFront;
import org.team1251.frc.robot.commands.football.Load;
import org.team1251.frc.robot.commands.football.RetractLoader;
import org.team1251.frc.robot.commands.football.RunEjector;
import org.team1251.frc.robot.commands.test.DriveBaseMotorTest;
import org.team1251.frc.robot.commands.test.LiftTest;
import org.team1251.frc.robot.commands.test.PneumaticTest;
import org.team1251.frc.robot.humanInterface.input.HumanInput;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robot.parts.mechanisms.MechanismFactory;
import org.team1251.frc.robot.parts.sensors.SensorFactory;
import org.team1251.frc.robot.subsystems.Climber;
import org.team1251.frc.robot.subsystems.DriveBase;
import org.team1251.frc.robot.subsystems.football.Ejector;
import org.team1251.frc.robot.subsystems.football.Loader;
import org.team1251.frc.robotCore.TigerTimedRobot;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.XBoxController;
import org.team1251.frc.robotCore.humanInterface.input.triggers.ButtonTrigger;
import org.team1251.frc.robotCore.humanInterface.input.triggers.LongPressTrigger;
import org.team1251.frc.robotCore.parts.sensors.LimeLight;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the `MAIN_CLASS` property in
 * the `build.gradle` file.
 */
public class Robot extends TigerTimedRobot {

    /**
     * How many milliseconds in each robot tick.
     */
    public static final int TICK_PERIOD_MS = 20;

    public static final ControllerFactory controllerFactory = new ControllerFactory();
    public static final MechanismFactory mechanismFactory = new MechanismFactory();
    public static final SensorFactory sensorFactory = new SensorFactory();



    /**
     * The source of all input that comes from the human players.
     */
    private HumanInput humanInput;

    /**
     * The subsystem that moves the robot around the field.
     */
    private DriveBase driveBase;

    /**
     * The subsystem that controls the climb Elevators
     */
    private Climber climber;

    /**
     * The command that gives the human players the ability to move the robot around the field.
     */
    private TeleopDrive teleopDrive;

    private LimeLight limelight;

    /**
     * The game pad used as input during test.
     */
    private XBoxController testerGamePad;

    /**
     * A command used to individually test motors.
     */
    private DriveBaseMotorTest motorTestCmd;
    private Climb climbLvl3;
    private Climb climbLvl2;
    private boolean wasTestModeActivated = false;
    private PneumaticTest pneumaticsTestCmd;
    private LiftTest liftTestCmd;
    private AbandonClimb abandonClimb;

    private Ejector footballEjector;
    private Loader footballLoader;
    private LiftFront liftFrontCmd;
    private Load loadFootballCmd;
    private RetractLoader retractFootballLoaderCmd;
    private RunEjector runFootballEjector;

    private NetworkTableEntry dashLeftEjectorWheelPowerCtl;
    private NetworkTableEntry dashRightEjectorWheelPowerCtl;
    private NetworkTableEntry dashFrontLiftInchesCtl;
    private NetworkTable footballTable;

    /**
     * Creates the robot!
     */
    public Robot() {
        super(TICK_PERIOD_MS / 1000.0);
    }

    /**
     * Does any work that must be done before an other initialization methods.
     *
     * Anything that needs to happen before everything else should be done here.
     *
     * This method is useful for performing tasks which don't "fit" into other `robotInit*()` methods and don't rely
     * on objects created in those methods.
     *
     * This is always invoked immediately before {@link #createSharedSensors()}}.
     */
    @Override
    protected void robotInitPrep() { }

    /**
     * Creates all sensors that need to be accessed by multiple parts of the robot. These are different from sensors
     * that are isolated to a specific mechanism or subsystem.
     *
     * These are the first to be created because they may be utilized by any other component (human interfaces,
     * subsystems, or commands).
     *
     * This is always invoked immediately before {@link #createSubsystems()} and after {@link #robotInitPrep()}};
     */
    @Override
    protected void createSharedSensors() {
        limelight = new LimeLight();
        limelight.setCameraMode(LimeLight.CameraMode.DRIVER);
    }

    /**
     * Creates interfaces used for interaction with humans. This may be things which allow the humans to provide
     * instructions to the robot or allow the robot to provide feedback to the human.
     *
     * May rely on feedback systems since they have already been created. Subsystems or Commands may rely on these
     * since they are created later.
     *
     * This is always invoked immediately before {@link #createSubsystems()} and after {@link #createSharedSensors()}};
     */
    @Override
    protected void createHumanInterfaces() {
        humanInput = new HumanInput();

        footballTable = NetworkTableInstance.getDefault().getTable("Football");
        dashLeftEjectorWheelPowerCtl = footballTable.getEntry("Left Ejector Power");
        dashRightEjectorWheelPowerCtl = footballTable.getEntry("Right Ejector Power");
        dashFrontLiftInchesCtl = footballTable.getEntry("Lift Inches");

        dashLeftEjectorWheelPowerCtl.setDefaultDouble(.5);
        dashRightEjectorWheelPowerCtl.setDefaultDouble(.5);
        dashFrontLiftInchesCtl.setDefaultDouble(0.);
    }

    /**
     * Creates the robot's subsystems.
     *
     * Feedback systems and human interfaces are now available for use as dependencies. Commands are not yet available,
     * so default commands are set into subsystems later.
     *
     * This is always invoked immediately before {@link #createCommands()} and after {@link #createHumanInterfaces()};
     */
    @Override
    protected void createSubsystems() {
        driveBase = new DriveBase();
        climber = new Climber();
    }

    /**
     * Creates the commands which make the robot do stuff.
     *
     * Feedback systems, human interfaces, and subsystems are now available for use as dependencies.
     *
     * This is always invoked immediately before {@link #robotInitFinalize()} and after {@link #createSubsystems()};
     */
    @Override
    protected void createCommands() {
        teleopDrive = new TeleopDrive(driveBase, humanInput);
        climbLvl3 = new Climb(driveBase, climber, Climber.LiftTarget.HAB_LVL_3);
        climbLvl2 = new Climb(driveBase, climber, Climber.LiftTarget.HAB_LVL_2);
        abandonClimb = new AbandonClimb(climbLvl2, climbLvl3);
    }

    /**
     * Executed as the final step in robot initialization.
     * 
     * This method is useful for solving certain chicken/egg problems, especially circular dependencies. For example,
     * this is a good place to set default subsystem commands -- default commands rely on their subsystems and
     * subsystems rely on their default commands so they have a circular dependency.
     * 
     * NOTE: If you have default commands that change based on game mode, use the appropriate `*EveryInit()` method.
     * This can avoid confusing behavior, especially during practice sessions where you switch modes back and
     * forth several times without restarting the robot!
     * 
     * This method is also useful for creating any additional objects which do not "fit" into the other `robotInit*()`
     * methods and rely on objects created by those methods.
     * 
     * This is always invoked immediately after {@link #createCommands()}. By default, this method does nothing.
     */
    @Override
    protected void robotInitFinalize() {
        telemetrySender.add(climber);
    }

    /**
     * Called every time the autonomous game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void onFirstAutonomousActivation() { }


    /**
     * Called the first time the autonomous game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void onEveryAutonomousActivation() {
        if (wasTestModeActivated) {
            throw new RuntimeException("Can not run autonomous after test mode - restart robot code from driver station!");
        }

        driveBase.setDefaultCommand(teleopDrive);
    }


    /**
     * Called the first time the teleop game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void onEveryTeleopActivation() {
        if (wasTestModeActivated) {
            throw new RuntimeException("Can not run teleop after test mode - restart robot code from driver station!");
        }

        driveBase.setDefaultCommand(teleopDrive);
    }

    /**
     * Called every time the teleop game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void onFirstTeleopActivation() {
        humanInput.attachCommandTriggers(climbLvl3, climbLvl2, abandonClimb);
    }

    /**
     * Called the every time the test game mode is activated.
     */
    @Override
    protected void onEveryTestActivation() {
        // Turn off live window, we don't use it. This will re-enable our ability to use the command-based system
        // while in test mode.
        LiveWindow.setEnabled(false);
        motorTestCmd.reset(null);
        wasTestModeActivated = true;

        limelight.setCameraMode(LimeLight.CameraMode.DRIVER);
        limelight.setLedMode(LimeLight.LedMode.OFF);
    }

    /**
     * Called first time the test game mode is activated.
     */
    @Override
    protected void onFirstTestActivation() {
        testerGamePad = humanInput.getDriverPad();

        footballEjector = new Ejector();
        footballLoader = new Loader();

        liftFrontCmd = new LiftFront(climber, dashFrontLiftInchesCtl);
        loadFootballCmd = new Load(footballLoader);
        retractFootballLoaderCmd = new RetractLoader(footballLoader);
        runFootballEjector = new RunEjector(footballEjector, dashLeftEjectorWheelPowerCtl, dashRightEjectorWheelPowerCtl);

        footballLoader.setDefaultCommand(retractFootballLoaderCmd);
        (new ButtonTrigger(testerGamePad.start())).toggleWhenPressed(liftFrontCmd);
        (new ButtonTrigger(testerGamePad.start())).toggleWhenPressed(runFootballEjector);
        (new LongPressTrigger(new ButtonTrigger(testerGamePad.a()), .5)).toggleWhenPressed(loadFootballCmd);

        // Also wire in the actual test commands.
        motorTestCmd = new DriveBaseMotorTest(driveBase);
        pneumaticsTestCmd = new PneumaticTest(testerGamePad.y(), testerGamePad.x(), climber);
        liftTestCmd = new LiftTest(climber);

//        (new ButtonTrigger(testerGamePad.lt())).whileHeld(motorTestCmd);
        (new ButtonTrigger(testerGamePad.rt())).whileHeld(pneumaticsTestCmd);
        (new ButtonTrigger(testerGamePad.select())).whileHeld(liftTestCmd);
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    public void disabledInit() {
        // TODO: Clean up -- called once per disable.
    }
}
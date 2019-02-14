package org.team1251.frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.team1251.frc.robot.commands.*;
import org.team1251.frc.robot.commands.ElevatorShifters.DisablePanelElevator;
import org.team1251.frc.robot.commands.ElevatorShifters.EnablePanelElevator;
import org.team1251.frc.robot.commands.test.MotorTest;
import org.team1251.frc.robot.humanInterface.input.HumanInput;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robot.subsystems.CargoClarm;
import org.team1251.frc.robot.subsystems.DriveBase;
import org.team1251.frc.robot.subsystems.Elevators;
import org.team1251.frc.robot.subsystems.PanelClarm;
import org.team1251.frc.robotCore.TigerTimedRobot;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.GamePad;
import org.team1251.frc.robotCore.humanInterface.input.gamepad.ModernGamePad;
import org.team1251.frc.robotCore.humanInterface.input.triggers.ButtonTrigger;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the `MAIN_CLASS` property in
 * the `build.gradle` file.
 */
public class Robot extends TigerTimedRobot {

    /**
     * How fast to iterate.
     */
    private static final double TICK_PERIOD = .01;

    //Elevator Enable Flags
    public static boolean isClimbElevatorEnabled = false;
    public static boolean isCargoElevatorEnabled = false;
    public static boolean isPanelElevatorEnabled = false;

    /**
     * The device manager which is used to create various devices.
     *
     * The device manager will detect duplicate port assignments and throw meaningful exceptions instead of the
     * not-so-useful ones thrown by default.
     */
    public static DeviceManager deviceManager = new DeviceManager();

    /**
     * The source of all input that comes from the human players.
     */
    private HumanInput humanInput;

    /**
     * The subsystem that moves the robot around the field.
     */
    private DriveBase driveBase;

    /**
     * The subsystem that controls the Claw and Arm for Cargo.
     */

    private CargoClarm cargoClarm;

    /**
     * The subsystem that controls the Claw and Arm for Panel.
     */

    private PanelClarm panelClarm;

    /**
     * The subsystem that controls all of the Elevators.
     */
    private Elevators elevators;


    /**
     * A command used to extend the panel arm.
     */

    private ExtendPanelArm extendPanelArm;

    /**
     * A command used to retract the panel arm.
     */

    private RetractPanelArm retractPanelArm;

    /**
     * A command used to extend the panel arm.
     */

    private OpenPanelClaw openPanelClaw;

    /**
     * A command used to move Cargo Arm Up.
     */

    private MoveCargoArmUp moveCargoArmUp;

    /**
     * A command used to move Cargo Arm Down.
     */

    private MoveCargoArmDown moveCargoArmDown;

    /**
     * A command used to outtake Cargo.
     */

    private OuttakeCargo outtakeCargo;

    /**
     * A command used to move Intake Cargo.
     */

    private IntakeCargo intakeCargo;

    /**
     * A command used to drive the Climb Elevator Gearbox forward.
     */

    private DriveClimbElevatorGearboxForward driveClimbElevatorGearboxForward;

    /**
     * A command used to drive the Climb Elevator Gearbox reverse.
     */

    private DriveClimbElevatorGearboxReverse driveClimbElevatorGearboxReverse;

    /**
     * These Commands below will initialize all of the elevator shifters.
     * These are also known as the ,"I'm far too lazy to actually write a
     * description for each so I am grouping them. - KAZQ46
    **/

    private EnablePanelElevator enablePanelElevator;
    private DisablePanelElevator disablePanelElevator;

    //// TEST RELATED fields start here

    /**
     * The game pad used as input during test.
     */
    private GamePad testerGamePad;

    /**
     * A command used to individually test motors.
     */
    private MotorTest motorTestCmd;

    /**
     * Creates the robot!
     */
    public Robot() {
        super(TICK_PERIOD);
    }

    /**
     * Does any work that must be done before an other initialization methods.
     *
     * Anything that needs to happen before everything else should be done here.
     *
     * This method is useful for performing tasks which don't "fit" into other `robotInit*()` methods and don't rely
     * on objects created in those methods.
     *
     * This is always invoked immediately before {@link #robotInitCreateFeedbackSystems()}}.
     */
    @Override
    protected void robotInitPrep() { }

    /**
     * Creates all standalone feedback systems that will be used by the robot. These may be systems which provide
     * feedback to the robot or to the human players.
     *
     * These are the first to be created because it may be a dependency of anything else. However, because they are
     * created first, they may not use anything else as a dependency. For feedback systems that rely on particular
     * subsystems or commands, use the {@link #robotInitFinalize()} method.
     *
     * This is always invoked immediately before {@link #robotInitCreateSubsystems()} and after {@link #robotInitPrep()}};
     */
    @Override
    protected void robotInitCreateFeedbackSystems() { }

    /**
     * Creates interfaces used for interaction with humans. This may be things which allow the humans to provide
     * instructions to the robot or allow the robot to provide feedback to the human.
     *
     * May rely on feedback systems since they have already been created. Subsystems or Commands may rely on these
     * since they are created later.
     *
     * This is always invoked immediately before {@link #robotInitCreateSubsystems()} and after {@link #robotInitCreateFeedbackSystems()}};
     */
    @Override
    protected void robotInitCreateHumanInterfaces() {
        humanInput = new HumanInput();
    }

    /**
     * Creates the robot's subsystems.
     *
     * Feedback systems and human interfaces are now available for use as dependencies. Commands are not yet available,
     * so default commands are set into subsystems later.
     *
     * This is always invoked immediately before {@link #robotInitCreateCommands()} and after {@link #robotInitCreateHumanInterfaces()};
     */
    @Override
    protected void robotInitCreateSubsystems() {
        driveBase = new DriveBase();
        // TODO: Uncomment as they become available -- robot dies if we try to initialize devices that don't exist.
//        cargoClarm = new CargoClarm();
//        panelClarm = new PanelClarm();
//        elevators = new Elevators();
    }

    /**
     * Creates the commands which make the robot do stuff.
     *
     * Feedback systems, human interfaces, and subsystems are now available for use as dependencies.
     *
     * This is always invoked immediately before {@link #robotInitFinalize()} and after {@link #robotInitCreateSubsystems()};
     */
    @Override
    protected void robotInitCreateCommands() {
        // TODO: Uncomment once related subsystems are available.
//        extendPanelArm = new ExtendPanelArm(panelClarm);
//        retractPanelArm = new RetractPanelArm(panelClarm);
//        openPanelClaw = new OpenPanelClaw(panelClarm);
//        moveCargoArmUp = new MoveCargoArmUp(cargoClarm);
//        moveCargoArmDown = new MoveCargoArmDown(cargoClarm);
//        outtakeCargo = new OuttakeCargo(cargoClarm);
//        intakeCargo = new IntakeCargo(cargoClarm);
//        driveClimbElevatorGearboxForward = new DriveClimbElevatorGearboxForward(elevators);
//        driveClimbElevatorGearboxReverse = new DriveClimbElevatorGearboxReverse(elevators);
//
//        //Elevator Shifter Commands
//        enablePanelElevator = new EnablePanelElevator(elevators);
//        disablePanelElevator = new DisablePanelElevator(elevators);
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
     * This is always invoked immediately after {@link #robotInitCreateCommands()}. By default, this method does nothing.
     */
    @Override
    protected void robotInitFinalize() { }

    /**
     * Called every time the autonomous game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void autonomousFirstInit() { }


    /**
     * Called the first time the autonomous game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void autonomousEveryInit() { }


    /**
     * Called the first time the teleop game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void teleopEveryInit() { }

    /**
     * Called every time the teleop game mode is activated.
     *
     * Be fast here! The game is running at this point. Any slower tasks should happen during one of the robotInit*()
     * methods.
     */
    @Override
    protected void teleopFirstInit() { }

    /**
     * Called the first time the test game mode is activated.
     */
    @Override
    protected void testEveryInit() {
        // Turn off live window, we don't use it. This will re-enable our ability to use the command-based system
        // while in test mode.
        LiveWindow.setEnabled(false);
        motorTestCmd.reset();
    }

    /**
     * Called every time the test game mode is activated.
     */
    @Override
    protected void testFirstInit() {
        // Use port 4 for the tester game pad to make sure it does not conflict with the main game.
        testerGamePad = new ModernGamePad(new Joystick(4));
        motorTestCmd = new MotorTest(driveBase);
        (new ButtonTrigger(testerGamePad.x())).whileHeld(motorTestCmd);
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    public void disabledInit() {
        // TODO: Clean up -- called once per disable.
    }

    /**
     * Runs once per tick when the robot is disabled.
     */
    @Override
    public void disabledPeriodic() {
        // TODO: Should we really be running scheduler? (I think so, because it will forcibly shutdown commands.. maybe?)
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once per "tick" during the sandstorm.
     *
     * For the 2019 game, there is no strictly "autonomous" mode; it is replaced by the "sandstorm" which allows for
     * human input. From a software-perspective, it is still called "autonomous". The terms "sandstorm" and
     * "autonomous" may be used interchangeably for this robot, although "sandstorm" is preferred unless directly
     * referencing method names.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once per tick during operator control mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once per tick during test mode.
     */
    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }
}
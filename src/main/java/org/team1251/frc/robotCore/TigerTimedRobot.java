package org.team1251.frc.robotCore;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.team1251.frc.robotCore.humanInterface.output.TelemetryProviderList;
import org.team1251.frc.robotCore.humanInterface.output.TelemetryTables;

/**
 * This abstract base creates additional structure to the way the robot is initialized during various phases.
 *
 * The following methods will be will be called, in order, exactly once during robot initialization (i.e. during
 * {@link #robotInit()}). Robot initialization occurs exactly once when the robot is powered up, or whenever new code is
 * deployed.
 *
 *    - {@link #robotInitPrep()}
 *    - {@link #createSharedSensors()}
 *    - {@link #createHumanInterfaces()}
 *    - {@link #createSubsystems()}
 *    - {@link #createCommands()}
 *    - {@link #robotInitFinalize()}
 *
 * When a particular robot mode (autonomous, teleop, test) is activated for the FIRST time, the corresponding
 * `on*FirstActivation()` is called; the related `on*EveryActivation()` method is called EVERY time game mode is
 * activated, including the first activation. For example, the first time "teleop" is activated, the
 * {@link #onFirstTeleopActivation()} is called, followed by {@link #onEveryTeleopActivation()}. The second (third,
 * fourth, etc) time "telop" is activated (without resetting the robot or re-deploying code), only
 * {@link #onEveryTeleopActivation()} is called.
 *
 * The behavior of {@link #disabledInit()} is not modified by this class, so it is called every time the robot is
 * disabled.
 *
 * The behavior of the {@link #autonomousPeriodic()}, {@link #teleopPeriodic()}, {@link #testPeriodic()},
 * and {@link #disabledPeriodic()} are not modified by this class.
 *
 * A default implementation of {@link #robotPeriodic()} is provided by this class which runs the command scheduler,
 * then sends telemetry data.
 */
abstract public class TigerTimedRobot extends TimedRobot {

    /**
     * Indicates whether or not test mode has been previously initialized.
     */
    private boolean testInitialized = false;

    /**
     * Indicates whether or not autonomous mode has been previously initialized.
     */
    private boolean autonomousInitialized = false;
    
    /**
     * Indicates whether or not teleop mode has been previously initialized.
     */
    private boolean teleopInitialized = false;

    /**
     * Network tables used for telemetry data. Robots generally do not need to access this directly -- instead they
     * should be implementing `ITelemetryProvider` and adding them to the `telemetrySender`.
     *
     * @see org.team1251.frc.robotCore.humanInterface.output.ITelemetryProvider
     * @see #telemetrySender
     */
    protected final TelemetryTables telemetryTables = new TelemetryTables();

    /**
     * A list of telemetry providers. Each will be asked to send their telemetry data every robot period.
     *
     * Specifically, each will be called, in the order they are added, during `robotPeriodic()`
     */
    protected final TelemetryProviderList telemetrySender = new TelemetryProviderList();

    /**
     * Creates a new instance with a particular tick period.
     *
     * @param tickPeriod - The frequency (in seconds) for the robot execution loop.
     */
    public TigerTimedRobot(double tickPeriod) {
        super(tickPeriod);
    }

    @Override
    public void robotInit() {
                
         robotInitPrep();
         createSharedSensors();
         createHumanInterfaces();
         createSubsystems();
         createCommands();
         robotInitFinalize();
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
    protected abstract void robotInitPrep();

    /**
     * Creates all standalone feedback systems that will be used by the robot. These may be systems which provide
     * feedback to the robot or to the human players.
     *
     * These are the first to be created because it may be a dependency of anything else. However, because they are
     * created first, they may not use anything else as a dependency. For feedback systems that rely on particular
     * subsystems or commands, use the {@link #robotInitFinalize()} method.
     *
     * This is always invoked immediately before {@link #createSubsystems()} and after {@link #robotInitPrep()}};
     */
    protected abstract void createSharedSensors();

    /**
     * Creates interfaces used for interaction with humans. This may be things which allow the humans to provide
     * instructions to the robot or allow the robot to provide feedback to the human.
     *
     * May rely on feedback systems since they have already been created. Subsystems or Commands may rely on these
     * since they are created later.
     *
     * This is always invoked immediately before {@link #createSubsystems()} and after {@link #createSharedSensors()}};
     */
    protected abstract void createHumanInterfaces();

    /**
     * Creates the robot's subsystems.
     *
     * Feedback systems and human interfaces are now available for use as dependencies. Commands are not yet available,
     * so default commands are set into subsystems later.
     *
     * This is always invoked immediately before {@link #createCommands()} and after {@link #createHumanInterfaces()};
     */
    protected abstract void createSubsystems();

    /**
     * Creates the commands which make the robot do stuff.
     *
     * Feedback systems, human interfaces, and subsystems are now available for use as dependencies.
     *
     * This is always invoked immediately before {@link #robotInitFinalize()} and after {@link #createSubsystems()};
     */
    protected abstract void createCommands();

    /**
     * Executed as the final step in robot initialization.
     *
     * This method is useful for solving certain chicken/egg problems, especially circular dependencies. For example,
     * this is a good place to set default subsystem commands -- default commands rely on their subsystems and
     * subsystems rely on their default commands so they have a circular dependency.
     *
     * NOTE: If you have default commands that change based on game mode, use the appropriate `on*EveryActivation()`
     *       method. This can avoid confusing behavior, especially during practice sessions where you switch modes
     *       back and forth several times without restarting the robot!
     *
     * This method is also useful for creating any additional objects which do not "fit" into the other `robotInit*()`
     * methods and rely on objects created by those methods.
     *
     * This is always invoked immediately after {@link #createCommands()}. By default, this method does nothing.
     */
    protected abstract void robotInitFinalize();

    @Override
    public void autonomousInit() {

        // Auto should always be the first mode, if it is to run at all. Otherwise teleop or test commands may
        // have been attached to the human input causing unsafe or unexpected robot behavior.

        if (teleopInitialized) {
            DriverStation.reportError("Teleop mode was previously activated, refusing to run Autonomous mode. " +
                    "Forcing a crash to restart Robot Code. In the future restart the Robot Code before making " +
                    "this transition.", false);
            throw new RuntimeException("Cannot initialize Autonomous mode after Teleop mode.");
        }

        if (testInitialized) {
            DriverStation.reportError("Test mode was previously activated, refusing to run Autonomous mode. " +
                    "Forcing a crash to restart Robot Code. In the future restart the Robot Code before making " +
                    "this transition.", false);
            throw new RuntimeException("Cannot initialize Autonomous mode after Test mode.");
        }

        if (!autonomousInitialized) {
            autonomousInitialized = true;
            onFirstAutonomousActivation();
        }

        onEveryAutonomousActivation();
    }

    /**
     * Called the first the autonomous game mode is activated.
     */
    protected abstract void onFirstAutonomousActivation();

    /**
     * Called every time the autonomous game mode is activated.
     */
    protected abstract void onEveryAutonomousActivation();

    @Override
    public void teleopInit() {

        // Teleop should never run after test mode. Test commands may have been attached to the human input
        // causing unsafe or unexpected behavior.

        if (testInitialized) {
            DriverStation.reportError("Test mode was previously activated, refusing to run Teleop mode. " +
                    "Forcing a crash to restart Robot Code. In the future restart the Robot Code before making " +
                    "this transition.", false);
            throw new RuntimeException("Cannot initialize Teleop mode after Test mode.");
        }

        if (!teleopInitialized) {
            teleopInitialized = true;
            onFirstTeleopActivation();
        }

        onEveryTeleopActivation();
    }

    /**
     * Called the first time the teleop game mode is activated.
     */
    protected abstract void onFirstTeleopActivation();

    /**
     * Called every time the teleop game mode is activated.
     */
    protected abstract void onEveryTeleopActivation();


    @Override
    public void testInit() {

        // Test mode should never run after autonomous or teleop. Standard commands may have been attached to the human
        // input causing unsafe or unexpected behavior.

        if (autonomousInitialized) {
            DriverStation.reportError("Autonomous mode was previously activated, refusing to run Test mode. " +
                    "Forcing a crash to restart Robot Code. In the future restart the Robot Code before making " +
                    "this transition.", false);
            throw new RuntimeException("Cannot initialize Test mode after Autonomous mode.");
        }

        if (teleopInitialized) {
            DriverStation.reportError("Teleop mode was previously activated, refusing to run Test mode. " +
                    "Forcing a crash to restart Robot Code. In the future restart the Robot Code before making " +
                    "this transition.", false);
            throw new RuntimeException("Cannot initialize Test mode after Teleop mode.");
        }

        if (!testInitialized) {
            testInitialized = true;
            onFirstTestActivation();
        }

        onEveryTestActivation();
    }

    /**
     * Called the first time the test game mode is activated.
     */
    protected abstract void onFirstTestActivation();

    /**
     * Called every time the test game mode is activated.
     */
    protected abstract void onEveryTestActivation();

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        telemetrySender.sendTelemetryData(telemetryTables);
    }
}

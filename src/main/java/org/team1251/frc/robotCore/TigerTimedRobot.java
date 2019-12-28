package org.team1251.frc.robotCore;

import edu.wpi.first.wpilibj.TimedRobot;
import org.team1251.frc.robotCore.humanInterface.feedback.TelemetryProviderList;
import org.team1251.frc.robotCore.humanInterface.feedback.TelemetryTables;

/**
 * This abstract base creates additional structure to the way the robot is initialized during various phases.
 *
 * The following methods will be will be called, in order, exactly once during robot initialization (e.g. during
 * {@link #robotInit()}). Robot initialization occurs exactly once when the robot is powered up, or whenever new code is
 * deployed.
 *
 *    - {@link #robotInitPrep()}
 *    - {@link #robotInitCreateFeedbackSystems()}
 *    - {@link #robotInitCreateHumanInterfaces()}
 *    - {@link #robotInitCreateSubsystems()}
 *    - {@link #robotInitCreateCommands()}
 *    - {@link #robotInitFinalize()}
 *
 * When a particular game mode (autonomous, teleop, test) is activated for the FIRST time, the corresponding
 * `*FirstInit()` is called; the `*EveryInit()` method is called EVERY time game mode is activated. If it is the first
 * activation of the game mode, `*EveryInit()` will be called immediately after `*FirstInit()`.  For example, the first
 * time "teleop" is activated, the {@link #teleopFirstInit()} is called, followed by {@link #teleopEveryInit()}. The
 * second time "telop" is activated (without resetting the robot or re-deploying code), only `teleopEveryInit()` is
 * called.
 *
 * The behavior of {@link #disabledInit()} is not modified by this class, so it is called every time the robot is
 * disabled.
 *
 * The behavior of the {@link #robotPeriodic()}, {@link #autonomousPeriodic()}, {@link #teleopPeriodic()},
 * {@link #testPeriodic()}, and {@link #disabledPeriodic()} are not modified by this class.
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
     * @see org.team1251.frc.robotCore.humanInterface.feedback.ITelemetryProvider
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
         robotInitCreateFeedbackSystems();
         robotInitCreateHumanInterfaces();
         robotInitCreateSubsystems();
         robotInitCreateCommands();
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
     * This is always invoked immediately before {@link #robotInitCreateFeedbackSystems()}}.
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
     * This is always invoked immediately before {@link #robotInitCreateSubsystems()} and after {@link #robotInitPrep()}};
     */
    protected abstract void robotInitCreateFeedbackSystems();

    /**
     * Creates interfaces used for interaction with humans. This may be things which allow the humans to provide
     * instructions to the robot or allow the robot to provide feedback to the human.
     *
     * May rely on feedback systems since they have already been created. Subsystems or Commands may rely on these
     * since they are created later.
     *
     * This is always invoked immediately before {@link #robotInitCreateSubsystems()} and after {@link #robotInitCreateFeedbackSystems()}};
     */
    protected abstract void robotInitCreateHumanInterfaces();

    /**
     * Creates the robot's subsystems.
     *
     * Feedback systems and human interfaces are now available for use as dependencies. Commands are not yet available,
     * so default commands are set into subsystems later.
     *
     * This is always invoked immediately before {@link #robotInitCreateCommands()} and after {@link #robotInitCreateHumanInterfaces()};
     */
    protected abstract void robotInitCreateSubsystems();

    /**
     * Creates the commands which make the robot do stuff.
     *
     * Feedback systems, human interfaces, and subsystems are now available for use as dependencies.
     *
     * This is always invoked immediately before {@link #robotInitFinalize()} and after {@link #robotInitCreateSubsystems()};
     */
    protected abstract void robotInitCreateCommands();

    /**
     * Executed as the final step in robot initialization.
     *
     * This method is useful for solving certain chicken/egg problems, especially circular dependencies. For example,
     * this is a good place to set default subsystem commands -- default commands rely on their subsystems and
     * subsystems rely on their default commands so they have a circular dependency.
     *
     * NOTE: If you have default commands that change based on game mode, use the appropriate `*EveryInit()` method.
     *       This can avoid confusing behavior, especially during practice sessions where you switch modes back and
     *       forth several times without restarting the robot!
     *
     * This method is also useful for creating any additional objects which do not "fit" into the other `robotInit*()`
     * methods and rely on objects created by those methods.
     *
     * This is always invoked immediately after {@link #robotInitCreateCommands()}. By default, this method does nothing.
     */
    protected abstract void robotInitFinalize();

    @Override
    public void autonomousInit() {
        if (!autonomousInitialized) {
            autonomousInitialized = true;
            autonomousFirstInit();
        }

        autonomousEveryInit();
    }

    /**
     * Called the first time the autonomous game mode is activated.
     */
    protected abstract void autonomousEveryInit();

    /**
     * Called every time the autonomous game mode is activated.
     */
    protected abstract void autonomousFirstInit();

    @Override
    public void teleopInit() {
        if (!teleopInitialized) {
            teleopInitialized = true;
            teleopFirstInit();
        }

        teleopEveryInit();
    }

    /**
     * Called the first time the teleop game mode is activated.
     */
    protected abstract void teleopEveryInit();

    /**
     * Called every time the teleop game mode is activated.
     */
    protected abstract void teleopFirstInit();


    @Override
    public void testInit() {
        if (!testInitialized) {
            testInitialized = true;
            testFirstInit();
        }

        testEveryInit();
    }

    /**
     * Called the first time the test game mode is activated.
     */
    protected abstract void testEveryInit();

    /**
     * Called every time the test game mode is activated.
     */
    protected abstract void testFirstInit();

    @Override
    public void robotPeriodic() {
        telemetrySender.sendTelemetryData(telemetryTables);
    }
}

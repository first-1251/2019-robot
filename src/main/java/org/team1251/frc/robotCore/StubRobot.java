package org.team1251.frc.robotCore;

/**
 * An example Robot implementation. You can copy this and use it as the template for your `Robot` class.
 */
final public class StubRobot extends TigerTimedRobot {

    private static final double TICK_PERIOD = .02; // 50 ticks per second.

    public StubRobot() {
        super(TICK_PERIOD);
    }

    @Override
    protected void robotInitPrep() {
        // This happens first when the robot initializes.
        // TODO: Any work that must happen before feedback systems are created.
    }

    @Override
    protected void createSharedSensors() {
        // This happens after `robotInitPrep()` and right before `createHumanInterfaces()`
        // TODO: Create feedback systems such as sensor devices that are shared by multiple subsystems
    }

    @Override
    protected void createHumanInterfaces() {
        // This happens after `createFeedbackSystems()` and right before `createSubSystems()`
        // TODO: Create ways for humans to provide instructions or get feedback from the Robot.
    }

    @Override
    protected void createSubsystems() {
        // This happens after `createHumanInterfaces() and right before `createCommands()`
        // TODO: Create all subsystems.
    }

    @Override
    protected void createCommands() {
        // This happens after `createSubsystems()` and right before `createCommands()`
        // TODO: Create all commands
    }

    @Override
    protected void robotInitFinalize() {
        // This happens after `createCommands()` and is the last chance to do robot initialization tasks.
        // TODO: Assign default commands to subsystems that apply to ALL Robot modes
        // TODO: Attach command triggers that apply to ALL Robot modes
        // TODO: Add telemetry providers (usually subsystems or feedback systems) to telemetrySender
        // TODO: Do any remaining initialization tasks that need to be done before ALL Robot modes.
    }

    @Override
    protected void onFirstAutonomousActivation() {
        // TODO: tasks that need to be done ONLY the first time autonomous mode is activated.
    }

    @Override
    protected void onEveryAutonomousActivation() {
        // TODO: tasks that need to be done EVERY time (including the first) autonomous mode is activated.
    }

    @Override
    protected void onFirstTeleopActivation() {
        // TODO: tasks that need to be done ONLY the first time autonomous mode is activated.
    }

    @Override
    protected void onEveryTeleopActivation() {
        // TODO: tasks that need to be done EVERY time (including the first) teleop mode is activated.
    }

    @Override
    protected void onFirstTestActivation() {
        // TODO: tasks that need to be done ONLY the first time autonomous mode is activated.
    }

    @Override
    protected void onEveryTestActivation() {
        // Command scheduler and LiveWindow can not be active at the same time. LiveWindow is automatically
        // enabled in test mode, so make sure to disable it if you plan on using Commands for testing.
        // LiveWindow.setEnabled(false);

        // TODO: tasks that need to be done EVERY time (including the first) test mode is activated.
    }

    @Override
    public void disabledPeriodic() {
        super.disabledPeriodic();

        // TODO: Anything extra that needs to be done on every tick where the robot is disabled.
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();

        // TODO: Anything extra that needs to be done on every tick where the robot is running autonomous mode.
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();

        // TODO: Anything extra that needs to be done on every tick where the robot is running teleop mode.
    }

    @Override
    public void testPeriodic() {
        super.testPeriodic();

        // TODO: Anything extra that needs to be done on every tick where the robot is running test mode.
    }

    @Override
    public void robotPeriodic() {
        // This happens AFTER the mode-specific periodic methods.

        // Call parent to make sure command scheduler and telemetrySender operate.
        super.robotPeriodic();

        // TODO: Additional extra that needs to be done on every robot tick, regardless of current mode.
    }
}

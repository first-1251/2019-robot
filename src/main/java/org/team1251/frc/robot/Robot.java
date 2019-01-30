package org.team1251.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.team1251.frc.robot.commands.test.MotorTest;
import org.team1251.frc.robot.humanInterface.input.HumanInput;
import org.team1251.frc.robot.robotMap.DeviceManager;
import org.team1251.frc.robot.subsystems.DriveBase;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the `MAIN_CLASS` property in
 * the `build.gradle` file.
 */
public class Robot extends TimedRobot {

    /**
     * How fast to iterate.
     */
    private static final double TICK_PERIOD = .01;

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
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     *
     * NOTE: Things that are *only* used during "test mode" should NOT be created here -- it would be a waste of
     * resources since test mode is not part of competition; use {@link #testInit()} instead.
     */
    public void robotInit() {
        // Build the feedback systems first. All other things, including human interfaces may rely on these.
        createFeedbackSystems();

        // Create the human input/output interfaces. These may rely on feedback systems, commands and (less often)
        // subsystems may rely on the human interfaces.
        createHumanInterfaces();

        // Create the robot's subsystems. These may rely on feedback systems and (less often) human interfaces.
        createSubsystems();

        // Create the commands. These may rely on feedback systems, human input, and/or subsystems.
        createCommands();
    }

    /**
     * Creates all feedback systems that will be used by the robot. These may be systems which provide feedback to
     * the robot or to the human players.
     */
    private void createFeedbackSystems() {
        // TODO: Create feedback systems and capture in class fields for later use.
    }

    /**
     * Creates interfaces used for interaction with humans. This may be things which allow the humans to provide
     * instructions to the robot or allow the robot to provide feedback to the human.
     *
     * Note: Use {@link #testInit()} to create human interfaces *only* used during test mode.
     */
    private void createHumanInterfaces() {
        humanInput = new HumanInput();
    }

    /**
     * Creates the robot's subsystems.
     *
     * Note: Use {@link #testInit()} to create subsystems *only* used during test mode.
     */
    private void createSubsystems() {
        driveBase = new DriveBase();
    }

    /**
     * Creates the commands which make the robot do stuff.
     *
     * Note: Use {@link #testInit()} to create commands only used during test mode.
     */
    private void createCommands() {
        // TODO: Make commands.
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
     * Gets the robot ready for sandstorm mode. This is fired once at the start of the sandstorm.
     *
     * For the 2019 game, there is no strictly "autonomous" mode; it is replaced by the "sandstorm" which allows for
     * human input. From a software-perspective, it is still called "autonomous". The terms "sandstorm" and
     * "autonomous" may be used interchangeably for this robot, although "sandstorm" is preferred unless directly
     * referencing method names.
     */
    @Override
    public void autonomousInit() {
        // TODO: Get ready to do stuff in Sandstorm mode.

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
     * This function is called once at the start of operator control mode.
     */
    @Override
    public void teleopInit() {
        // TODO: Cancel autonomous command(s)
        // TODO: Prepare for teleop.
    }

    /**
     * This function is called once per tick during operator control mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once at the beginning of test mode.
     */
    @Override
    public void testInit() {
        motorTestCmd = new MotorTest(driveBase);
    }

    /**
     * This function is called once per tick during test mode.
     */
    @Override
    public void testPeriodic() {

        // IMPORTANT: We don't use command triggers in test mode because there is no way to remove command triggers
        // once they have been attached.
        // TODO: Can we just nullify triggers and let garbage collection take care of it?
        if (humanInput.isMotorTestResetButtonPressed()) {
            motorTestCmd.reset();
        } else if (humanInput.isMotorTestButtonPressed()) {
            motorTestCmd.start();
        } else {
            motorTestCmd.cancel();
        }

        Scheduler.getInstance().run();
    }
}
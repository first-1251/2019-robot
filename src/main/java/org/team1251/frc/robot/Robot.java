package org.team1251.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.team1251.frc.robot.humanInterface.input.HumanInput;
import org.team1251.frc.robot.robotMap.DeviceManager;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

    private static final double TICK_PERIOD = .01;

    private final DeviceManager deviceManager = new DeviceManager();

    private HumanInput humanInput;

    public Robot() {
        super(TICK_PERIOD);
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        initDashboardInputs();
        humanInput = new HumanInput();
    }

    private void initDashboardInputs() {

        // TODO: Initialize input controls on the dashboard
        // TODO-2019: Learn new ShuffleBoard API
    }


    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    public void disabledInit() {
        // TODO: Clean up -- called once per disable.
    }

    @Override
    public void disabledPeriodic() {
        // TODO: Should we really be running scheduler? (I think so, because it will forcibly shutdown commands.. maybe?)
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
     * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
     * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
     * below the Gyro
     * <p>1
     * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
     * or additional comparisons to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {
        // TODO: Get ready to do autonomous stuff -- will auto mode even run?!
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // TODO: Cancel autonomous command -- will we even run autonomousPeriodic?
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
         // TODO: Get ready to test things.
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        // TODO: Test things.
    }

}
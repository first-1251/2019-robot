package org.team1251.frc.robot.subsystems.football;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Ejector extends Subsystem {

    private final TalonSRX spinController;
    private final TalonSRX launchController;

    private boolean isSmartDashboardInitialized = false;

    public Ejector() {
        ControllerFactory controllerFactory = Robot.controllerFactory;
        launchController = controllerFactory.createFootballEjectorLaunchController();
        spinController = controllerFactory.createFootballEjectorSpinController();
    }

    public void run(double launchPower, double spinPower) {
        launchController.set(ControlMode.PercentOutput, launchPower);
        spinController.set(ControlMode.PercentOutput, spinPower);
    }

    public void stop() {
        launchController.set(ControlMode.PercentOutput, 0);
        spinController.set(ControlMode.PercentOutput, 0);
    }
}

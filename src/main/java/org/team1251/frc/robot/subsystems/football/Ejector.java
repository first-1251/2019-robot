package org.team1251.frc.robot.subsystems.football;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robotCore.subsystems.Subsystem;

public class Ejector extends Subsystem {

    private final TalonSRX rightController;
    private final TalonSRX leftController;

    public Ejector() {
        ControllerFactory controllerFactory = Robot.controllerFactory;
        leftController = controllerFactory.createFootballEjectorLeftController();
        rightController = controllerFactory.createFootballEjectorRightController();
    }

    public void run(double leftPower, double rightPower) {
        leftController.set(ControlMode.PercentOutput, leftPower);
        rightController.set(ControlMode.PercentOutput, rightPower);
    }

    public void stop() {
        leftController.set(ControlMode.PercentOutput, 0);
        rightController.set(ControlMode.PercentOutput, 0);
    }
}

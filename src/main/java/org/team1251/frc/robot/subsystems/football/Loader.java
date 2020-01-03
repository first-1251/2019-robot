package org.team1251.frc.robot.subsystems.football;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team1251.frc.robot.Robot;
import org.team1251.frc.robot.parts.controllers.ControllerFactory;
import org.team1251.frc.robotCore.subsystems.TigerSubsystem;

public class Loader extends TigerSubsystem {

    private final DoubleSolenoid solenoid;
    private final DoubleSolenoid.Value solenoidLoadValue = DoubleSolenoid.Value.kForward;
    private final DoubleSolenoid.Value getSolenoidRetractValue = DoubleSolenoid.Value.kReverse;


    public Loader() {
        ControllerFactory controllerFactory = Robot.controllerFactory;
        solenoid = controllerFactory.createFootballLoaderSolenoid();
    }

    public void retract() {
        solenoid.set(getSolenoidRetractValue);
    }

    public void load() {
        solenoid.set(solenoidLoadValue);
    }

}

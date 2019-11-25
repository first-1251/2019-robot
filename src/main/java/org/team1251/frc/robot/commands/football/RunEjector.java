package org.team1251.frc.robot.commands.football;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1251.frc.robot.subsystems.football.Ejector;

public class RunEjector extends Command {

    private final Ejector ejector;
    private double spinPower;
    private double launchPower;

    public RunEjector(Ejector ejector) {
        this.ejector = ejector;

        requires(ejector);
    }

    @Override
    protected void initialize() {
        // Fetch current value in case it is already on the dashboard.
        fetchSpeeds();

        // Make sure value is on dashboard, by writing current values
        SmartDashboard.putNumber("ejectorSpinPower", spinPower);
        SmartDashboard.putNumber("ejectorLaunchPower", launchPower);
    }

    @Override
    protected void execute() {
        fetchSpeeds();
        ejector.run(launchPower, spinPower);
    }

    @Override
    protected void end() {
        ejector.stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private void fetchSpeeds() {
        spinPower = SmartDashboard.getNumber("ejectorSpinPower", spinPower);
        launchPower = SmartDashboard.getNumber("ejectorLaunchPower", launchPower);
    }
}

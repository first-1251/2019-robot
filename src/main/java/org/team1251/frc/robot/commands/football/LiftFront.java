package org.team1251.frc.robot.commands.football;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team1251.frc.robot.subsystems.Climber;

public class LiftFront extends Command {

    private final Climber climber;
    private double distance;

    public LiftFront(Climber climber) {
        // Once this command is running, it is king until it is finished or has been cancelled. Other climber commands
        // are unsafe once this is running because they expect the climber to be in its starting configuration.
        setInterruptible(false);

        this.climber = climber;
        requires(climber);
    }

    @Override
    protected void initialize() {
        // Fetch current value in case it is already on the dashboard.
        fetchDistance();

        // Make sure value is on dashboard, by writing current values
        SmartDashboard.putNumber("frontLift", distance);
    }

    @Override
    protected void execute() {
        fetchDistance();
        climber.liftFront(distance);
    }

    @Override
    protected void end() {
        climber.killLiftMotors();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private void fetchDistance() {
        double value = SmartDashboard.getNumber("ejectorSpinPower", distance);
        distance = Math.max(0, Math.min(6, value)); // Clamp to value between 0 and 6
    }
}

package org.team1251.frc.robot.commands.football;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import org.team1251.frc.robot.subsystems.Climber;

public class LiftFront extends Command {

    private final Climber climber;
    private final NetworkTableEntry distanceCtl;
    private double distance = 0;

    public LiftFront(Climber climber, NetworkTableEntry distanceCtl) {
        // Once this command is running, it is king until it is finished or has been cancelled. Other climber commands
        // are unsafe once this is running because they expect the climber to be in its starting configuration.
        setInterruptible(false);

        this.climber = climber;
        requires(climber);

        this.distanceCtl = distanceCtl;
    }

    @Override
    protected void execute() {
        distance = distanceCtl.getDouble(distance);
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
}

package org.team1251.frc.robot.feedback;


import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Gyro {

    private final AHRS navx;
    private boolean isReady = false;

    public Gyro() {
        navx = new AHRS(SPI.Port.kMXP);
    }

    public boolean isReady() {
        if (isReady) {
            return true;
        }

        if (!navx.isCalibrating()) {
            navx.zeroYaw();
            isReady = true;
            return true;
        }

        return false;
    }

    /**
     *
     * @return -179..180
     */
    public double getHeading() {
        double heading = navx.getAngle() % 360;
        if (heading < 0) {
            heading += 360;
        }

        if (heading > 180) {
            heading -= 360;
        }

        return heading;
    }

}

package org.team1251.frc.robot.feedback;

public class PathUtils {

    private static double CAMERA_HEIGHT = 50;  // Distance of camera off floor, in inches
    private static double CAMERA_ANGLE_DOWNWARDS = 16; // Angle of camera's vertical tilt towards target

    // Height of center of target. (PoC only, real code will need variable height based on target).
    private static double TARGET_HEIGHT = 29;
    private final Gyro gyro;
    private final LimeLight limeLight;

    public class Position2D {
        /**
         * The distance the robot would need to shift left (negative) or right (postive) to be lined up with the target.
         */
        private final double x;

        /**
         * The distance the robot would need to shift backward (negative) or forward (postive) to be lined up with the
         * target.
         */
        private final double y;

        private final double robotHeading;
        private final double targetPlaneHeading;

        Position2D(double x, double y, double robotHeading, double targetPlaneHeading) {
            this.x = x;
            this.y = y;
            this.robotHeading = robotHeading;
            this.targetPlaneHeading = targetPlaneHeading;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getRobotHeading() {
            return robotHeading;
        }

        public double getTargetPlaneHeading() {
            return targetPlaneHeading;
        }

        @Override
        public String toString() {
            return "Position2D: " + "distanceX: " + x + " | distanceY: " + y + " | robotHeading: " + robotHeading + " | targetPlaneHeading: " + targetPlaneHeading;
        }
    }

    public PathUtils(Gyro gyro, LimeLight limeLight) {

        this.gyro = gyro;
        this.limeLight = limeLight;
    }

    public Position2D calculateDistance2D(Target target) {

        if (!limeLight.hasTarget()) {
            throw new RuntimeException("No visible target!");
        }
        double robotHeading = gyro.getHeading();
        double targetVertOffset = limeLight.getVerticalOffset();
        double targetHorizOffset = limeLight.getHorizontalOffset();

        // Assume camera is above target. We can calculate the angle to the target by adding our downward angle to
        // the vertical target offset.  Relative to that angle, we know the *opposite* side based on the height of the
        // target and the height of the camera. We want to find the adjacent side (not the hypotenuse) -- so we want
        // to use TAN (t = opposite/angle)
        double distanceY = (CAMERA_HEIGHT - TARGET_HEIGHT) / Math.tan(Math.toRadians(CAMERA_ANGLE_DOWNWARDS - targetVertOffset));


        // Get the heading and use it to derive the most likely heading to point at the target plane
        double targetPlaneHeading;
        if (target == Target.SHIP) {
            // Target is either at -90, 90, or 0  Find out which we are closest to.
            if (robotHeading >= -135 && robotHeading <= -46) {
                targetPlaneHeading = -90;
            } else if (robotHeading >= -45 && robotHeading <= 44 ) {
                targetPlaneHeading = 0;
            } else if (robotHeading >= 45 && robotHeading <= 135 ) {
                targetPlaneHeading = 90;
            } else {
                throw new RuntimeException("Can not figure out what you want -- point more towards target!");
            }
        } else {
            // TODO: Target is either at -135, -90, -45, 135, 90, or 45 (confirm angles!)
            throw new RuntimeException("Rocket targeting not implemented!");
        }

        // Adjust to shift angles as if we were heading straight at the target plane
        double headingAdjust = targetPlaneHeading - robotHeading;
        double adjustedTargetHorizOffset = targetHorizOffset - headingAdjust;

        // Our adjusted target offset is opposite our already-calculated distanceY, we need to find our adjacent side.
        // Once again, we can use tangent:
        //  tan = opposite / adjacent
        //  adjacent = opposite / tan
        //
        // Note, target offset may be positive or negative, so use absolute value for this calculation.
        double distanceX = distanceY / Math.tan(Math.toRadians(Math.abs(adjustedTargetHorizOffset)));


        return new Position2D(distanceX, distanceY, robotHeading, targetPlaneHeading);
    }

    public enum Target { SHIP, ROCKET }


}

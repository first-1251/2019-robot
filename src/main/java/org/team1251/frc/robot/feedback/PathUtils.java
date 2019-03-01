package org.team1251.frc.robot.feedback;

public class PathUtils {


    // TODO: Calculate real value for ZERO_VERT_OFFSET_DISTANCE !
    private static double ZERO_NET_VERT_OFFSET_DISTANCE = 24; // Distance from target when target is centered vertically.
    private static double NET_VERT_OFFSET_EPSILON = .001; // Distance from value to consider negligible for net vertical offset.
    private static double NET_HORIZ_OFFSET_EPSILON = .001; // Distance from value to consider negligible for net vertical offset.


    // Height of center of target.
    // TODO: This is the ship target height (same as human station) - provide different height for rocket.
    private static double TARGET_HEIGHT = 29;

    // TODO: Figure out the appropriate place to use CAMERA_INSET, CAMERA_OFFSET, and BUMPER_DEPTH
    // TODO: Set accurate values for physical camera/robot attributes
    private static double CAMERA_ANGLE_DOWNWARDS = 16; // Angle of camera's vertical tilt towards target (degrees)
    private static double CAMERA_HEIGHT = 50;  // Distance of camera off floor, in inches
    private static double CAMERA_INSET = 0; // How far back the camera lens is from the front edge of the robot frame (inches)
    private static double CAMERA_OFFSET = 0; // How far off-center the camera lens is (inches; negative = left; positive = right;)
    private static double BUMPER_DEPTH = 0; // The depth of the FRONT side of the bumpers.

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
        double distance;
        double netVerticalOffset = CAMERA_ANGLE_DOWNWARDS - targetVertOffset;
        if (Math.abs(netVerticalOffset) < NET_VERT_OFFSET_EPSILON) {
            // Our net offset is very close to zero. The distance trig won't work, use a pre-calculated distance.
            distance = ZERO_NET_VERT_OFFSET_DISTANCE;
        } else {
            // Calculate the distance.
            distance = (CAMERA_HEIGHT - TARGET_HEIGHT) / Math.tan(Math.toRadians(netVerticalOffset));
        }

        // TODO: Figure out the intent of the driver based on the current robot heading and the known headings that
        //       would point us directly at a target plane (different "known headings" for ship vs. rocket). For example,
        //       if the SHIP is being targeted and the robot is at 80 degrees, they are probably targeting left side
        //       of the SHIP since they are much closer to 90 degrees than to 0 or -90(a.k.a 270). For ROCKET targeting,
        //       the
        double targetPlaneHeading = 0;

        // We have the distance between the camera and the target along the floor, but we need to get our x/y position
        // relative to the target. This requires more triangles! We can use the distance we already have as the
        // hypotenuse of a new right triangle; the length of the other two sides of the triangle will tell us our x/y
        // distances. We need another angle before we can figure out the sides, though. We can get it by combining
        // robot angle with the horizontal offset reported by the limelight.
        // TODO: Adjust angle of robot so that "0" represents the robot pointing straight at the target plane. Until
        //       then this only works for the target plane which we are facing when we have a natural heading of 0.
        double netHorizOffset = robotHeading + targetHorizOffset;

        // Short circuit the calculations if we are sitting right in front of the target.
        if (netHorizOffset < NET_HORIZ_OFFSET_EPSILON) {
            // Our distance is our y position and our x position is 0.
            // TODO: This is the x/y of the camera lens, account for offset within the robot frame (within bumper perimeter?)
            return new Position2D(0, distance, robotHeading, targetPlaneHeading);
        }

        // Keep track of the sign of the horizontal angle. This indicates whether we are sitting to the left or right
        // of the target and becomes the sign of our x position. More specifically, a negative value indicates we
        // are sitting to the right of the target and will have a positive x position and vice versa.
        int xSign = netHorizOffset < 0 ? 1 : -1;

        // Lose the sign on the adjusted horizontal angle, and convert it to radians.
        double netHorizOffsetRad = Math.toRadians(Math.abs(netHorizOffset));

        // We have the hypotenuse and one of the angles, we can now rely on SOH CAH TOA to figure out x and y.
        // x is opposite the known angle, so `SIN(angle) = x/h` or `x = SIN(angle) * h`
        // y is adjacent to the known angle, so `COS(angle) = y/h` or `y = COS(angle) * h`
        double x = Math.sin(netHorizOffsetRad) * distance;
        double y = Math.cos(netHorizOffsetRad) * distance;

        // TODO: This is the x/y of the camera lens, account for offset within the robot frame (within bumper perimeter?)
        return new Position2D(x * xSign, y, robotHeading, targetPlaneHeading);
    }

    public enum Target { SHIP, ROCKET }


}


package org.team1251.frc.robotCore.parts.sensors;

// TODO: Should write operations be part of a subsystem to avoid control conflicts?

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight {

    static private double IMAGE_CAPTURE_LATENCY = 11;


    private final NetworkTableEntry tx;
    private final NetworkTableEntry ty;
    private final NetworkTableEntry ta;
    private final NetworkTableEntry tv;
    private final NetworkTableEntry ts;
    private final NetworkTableEntry tl;
    private final NetworkTableEntry pipeline;
    private final NetworkTableEntry camMode;
    private final NetworkTableEntry ledMode;


    public enum LedMode {
        PIPELINE_DEFAULT, OFF, BLINK, ON
    }

    public enum CameraMode {
        CV, DRIVER
    }

    public LimeLight() {
        this("limelight");
    }

    public LimeLight(String id) {
        NetworkTable netTable = NetworkTableInstance.getDefault().getTable(id);

        tx = netTable.getEntry("tx");
        ty = netTable.getEntry("ty");
        ta = netTable.getEntry("ta");
        tv = netTable.getEntry("tv");
        ts = netTable.getEntry("ts");
        tl = netTable.getEntry("tl");

        ledMode = netTable.getEntry("ledMode");
        camMode = netTable.getEntry("camMode");
        pipeline = netTable.getEntry("pipeline");
    }


    public boolean hasTarget() {
        return tv.getDouble(0.0) > 0;
    }

    public double getHorizontalOffset() {
        return tx.getDouble(0.0);
    }

    public double getVerticalOffset() {
        return ty.getDouble(0.0);
    }

    /**
     * Percentage of image occupied by target
     */
    public double getTargetArea() {
        return ta.getDouble(0.0);
    }

    /**
     * Rotation/Skew of target in view.
     */
    public double getTargetSkew() {
        return ts.getDouble(0.0);
    }

    public double getLatency() {
        return IMAGE_CAPTURE_LATENCY + getPipelineLatency();
    }

    public double getPipelineLatency() {
        return tl.getDouble(0.0);
    }

    public void setLedMode(LedMode mode) {
        ledMode.setNumber(mode.ordinal());
    }

    public void setCameraMode(CameraMode mode) {
        camMode.setNumber(mode.ordinal());
    }

    public void setPipeline(int number) {
        pipeline.setNumber(number);
    }

    public Number getPipeline() {
        return pipeline.getNumber(0);
    }
}

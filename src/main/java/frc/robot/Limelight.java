package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

/**
 * An easy interface for accessing, controlling, and fetching data from the
 * Limelight. This class just wrappers the Limelight NetworkTables API in a
 * way that makes it easier to use, and much more Java-like.
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class Limelight {
    /* The name of the Limelight network table. */
    private String limelightTableName = "limelight";
    /* An instance of the Limelight table. */
    private NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable(limelightTableName);

    /**
     * Set the Limelight settings based on external factors and states.
     */
    public void update() {
        /* POV controls the stream, and the LEDs */
        switch (Robot.oi.getJoystickPOV()) {
            case UP:
              setStreamingMode(Limelight.StreamMode.PIP_MAIN);
              break;
            case DOWN:
              setStreamingMode(Limelight.StreamMode.PIP_SECONDARY);
              break;
            case LEFT:
              setLedMode(Limelight.LedState.FORCE_ON);
              break;
            case RIGHT:
              setLedMode(Limelight.LedState.FORCE_OFF);
            default:
              break;
          }
          /* The intake arm determines the pipeline. */
          if (Robot.intake.armIsUp()) {
              setPipeline(0);
          } else {
              setPipeline(1);
          }
    }

    /**
     * Fetch a value from the Limelight network table.
     * @param variableName The variable to get the value of.
     * @return A Java Double holding the value of the given variable, or 0, if
     * fetching the value fails.
     */
    private double getDouble(String variableName) {
        return limelightTable.getEntry(variableName).getDouble(0);
    }

    /**
     * Set a value in the Limelight network table.
     * @param variableName The variable to set.
     * @param value The value to set it to.
     */
    private void setNumber(String variableName, Number value) {
        limelightTable.getEntry(variableName).setNumber(value);
    }

    /**
     * Whether the Limelight has any valid targets (0 or 1)
     * @return True if there are targets in the sight of the Limelight.
     */
    public boolean hasValidTargets() {
        return (getDouble("tv") == 1);
    }

    /**
     * Horizontal offset from crosshair to target.
     * @return -27 degrees to 27 degrees.
     */
    public double getHorizontalOffset() {
        return getDouble("tx");
    }

    /**
     * Vertical offset from crosshair to target.
     * @return -20.5 degrees to 20.5 degrees.
     */
    public double getVerticalOffset() {
        return getDouble("ty");
    }

    /**
     * Target area.
     * @return 0% of image to 100% of image.
     */
    public double getTargetArea() {
        return getDouble("ta");
    }

    /**
     * Skew or rotation.
     * @return -90 degrees to 0 degrees.
     */
    public double getSkew() {
        return getDouble("ts");
    }

    /**
     * The pipeline's latency contribution (ms). Add at least 11ms for image
     * capture latency.
     * @return The pipeline latency of the current pipeline.
     */
    public double getPipelineLatency() {
        return getDouble("tl");
    }

    /**
     * Sidelength of shortest side of the fitted bounding box.
     * @return Pixels.
     */
    public double getShortSidelength() {
        return getDouble("tshort");
    }

    /**
     * Sidelength of the longest side of the fitted bounding box.
     * @return Pixels.
     */
    public double getLongSideLength() {
        return getDouble("tlong");
    }

    /**
     * Horizontal sidelength of the rough bounding box.
     * @return 0 - 320 pixels.
     */
    public double getHorizontalSideLength() {
        return getDouble("thor");
    }

    /**
     * Vertical sidelength of the rough bounding box.
     * @return 0 - 320 pixels.
     */
    public double getVerticalSideLength() {
        return getDouble("tvert");
    }

    /**
     * True active pipeline index of the camera.
     * @return 0 - 9
     */
    public double getPipe() {
        return getDouble("getpipe");
    }

    /**
     * Results of a 3D position solution.
     * @return 6 numbers: Translation (x,y,y), Rotation (pitch,yaw,roll)
     */
    public double getCamTran() {
        return getDouble("camtran");
    }

    /**
     * The possible LED states of the Limelight.
     */
    public static enum LedState {
        PIPELINE_CURRENT, FORCE_OFF, FORCE_BLINK, FORCE_ON
    }

    /**
     * Sets the Limelight's LED state.
     * @param state The LedState to set.
     */
    public void setLedMode(LedState state) {
        String ledModeKey = "ledMode";
        switch (state) {
            case PIPELINE_CURRENT:
                setNumber(ledModeKey, 0);
                break;
            case FORCE_OFF:
                setNumber(ledModeKey, 1);
                break;
            case FORCE_BLINK:
                setNumber(ledModeKey, 2);
                break;
            case FORCE_ON:
                setNumber(ledModeKey, 3);
                break;
        }
    }

    /**
     * The possible camera modes of the Limelight.
     */
    public static enum CameraMode {
        VISION, DRIVER
    }

    /**
     * Sets Limelight's operation mode.
     * @param mode The CameraMode to set.
     */
    public void setCameraMode(CameraMode mode) {
        String camModeKey = "camMode";
        switch (mode) {
            case VISION:
                setNumber(camModeKey, 0);
                break;
            case DRIVER:
                setNumber(camModeKey, 1);
                break;
        }
    }

    /**
     * Select pipeline.
     * @param pipeline 0 - 9
     */
    public void setPipeline(int pipeline) {
        if (pipeline >= 0 && pipeline <= 9) {
            setNumber("pipeline", pipeline);
        } else {
            throw new IllegalArgumentException("Pipeline out of bounds. Must be 0 - 9.");
        }
    }

    /**
     * The possible state of the Limelight's camera streaming mode.
     * <ul>
     *  <li>Standard - Side-by-side streams if a webcam is attatched to Limelight.</li>
     *  <li>PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream.</li>
     *  <li>PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream</li>
     * </ul>
     */
    public static enum StreamMode {
        STANDARD, PIP_MAIN, PIP_SECONDARY
    }

    /**
     * Set the Limelight's streaming mode.
     * @param mode The StreamMode to set.
     */
    public void setStreamingMode(StreamMode mode) {
        String streamKey = "stream";
        switch (mode) {
            case STANDARD:
                setNumber(streamKey, 0);
                break;
            case PIP_MAIN:
                setNumber(streamKey, 1);
                break;
            case PIP_SECONDARY:
                setNumber(streamKey, 2);
                break;
        }
    }

    /**
     * Allow users to take snapshots during a match.
     * @param doSnapshots True to take two snapshots per second, false to stop taking snapshots.
     */
    public void enableSnapshots(boolean doSnapshots) {
        String snapshotKey = "snapshot";
        if (doSnapshots) {
            setNumber(snapshotKey, 1);
        } else {
            setNumber(snapshotKey, 0);
        }
    }
}
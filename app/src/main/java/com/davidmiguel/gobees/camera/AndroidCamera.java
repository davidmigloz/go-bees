package com.davidmiguel.gobees.camera;

/**
 * AndroidCamera contract.
 */
public interface AndroidCamera {

    /**
     * Connects to the camera.
     */
    void connect();

    /**
     * Releases the camera.
     */
    void release();

    /**
     * Checks whether the camera is connected.
     *
     * @return camera status.
     */
    boolean isConnected();

    /**
     * Update the frame rate.
     *
     * @param delay  time to wait before issue the first frame (in milliseconds).
     * @param period period between frames (in milliseconds).
     */
    void updateFrameRate(long delay, long period);
}

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
}

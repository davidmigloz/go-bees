package com.davidmiguel.gobees.camera;

/**
 * Defines the methods that the camera can use to interact with the client.
 */
public interface ICameraAccess {

    /**
     * Checks if openCV is loaded.
     *
     * @return true / false.
     */
    boolean isOpenCVLoaded();

    /**
     * Callback when a frame is captured.
     *
     * @param CameraFrame frame.
     */
    void onPreviewFrame(CameraFrame CameraFrame);
}

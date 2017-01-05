package com.davidmiguel.gobees.camera;

/**
 * Defines the methods that the camera can use to interact with the client.
 */
public interface AndroidCameraListener {

    /**
     * Checks if openCV is loaded.
     *
     * @return true / false.
     */
    boolean isOpenCvLoaded();

    /**
     * This method is invoked when camera preview has started. After this method is invoked
     * the frames will start to be delivered to client via the onPreviewFrame() callback.
     * @param width width of the frames that will be delivered.
     * @param height height of the frames that will be delivered.
     */
    void onCameraStarted(int width, int height);

    /**
     * Callback when a frame is captured.
     *
     * @param cameraFrame frame.
     */
    void onPreviewFrame(CameraFrame cameraFrame);
}

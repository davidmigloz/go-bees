package com.davidmiguel.gobees.camera;

/**
 * Camera contract.
 */
public interface ICamera {
    void connect();

    void release();

    boolean isConnected();
}

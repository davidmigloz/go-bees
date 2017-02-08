/*
 * GoBees
 * Copyright (c) 2016 - 2017 David Miguel Lozano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.davidmiguel.gobees.monitoring;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of MonitoringPresenter.
 */
public class MonitoringPresenterTest {

    private static final long APIARY_ID = 1;
    private static final long HIVE_ID = 1;

    @Mock
    private MonitoringContract.View view;

    @Mock
    private MonitoringContract.SettingsView settingsView;

    @Mock
    private MonitoringSettings monitoringSettings;

    private MonitoringPresenter presenter;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new MonitoringPresenter(view, settingsView, APIARY_ID, HIVE_ID);

        // The presenter won't update the view unless it's active
        when(view.isActive()).thenReturn(true);
        when(settingsView.isActive()).thenReturn(true);
    }

    @Test
    public void onStartClicked_startMonitoring() {
        when(settingsView.getMonitoringSettings()).thenReturn(monitoringSettings);
        presenter.startMonitoring();
        // Config view and start monitoring
        verify(view).hideCameraView();
        verify(view).showMonitoringView();
        verify(view).startMonitoringService(eq(monitoringSettings));
        verify(view).bindMonitoringService();
    }

    @Test
    public void onStopClicked_stopMonitoring() {
        presenter.stopMonitoring();
        // Stop monitoring
        verify(view).stopMonitoringService();
    }

    @Test
    public void onSettingsClicked_openSettings() {
        presenter.openSettings();
        // Open settings view
        verify(settingsView).showSettings();
    }

    @Test
    public void onSettingsCloseClicked_closeSettings() {
        presenter.closeSettings();
        // Open settings view
        verify(settingsView).hideSettings();
    }

    @Test
    public void onOpenCvLoaded_showPreview() {
        presenter.onOpenCvConnected();
        // Show preview
        verify(view).enableCameraView();
    }

    @Test
    public void showAlgoOutput_showAlgoPreview() {
        presenter.showAlgoOutput(true);
        verify(view).showNumBeesView(eq(true));
    }

    @Test
    public void onZoomUpdated_updateCameraZoom() {
        presenter.updateAlgoZoom(0);
        verify(view).updateAlgoZoom(eq(0));
    }

    @Test
    public void onStartAndServiceNotRunning_startService() {
        presenter.start(true);
        // Show preview
        verify(view).bindMonitoringService();
        verify(view).hideCameraView();
        verify(view).showMonitoringView();
    }

    @Test
    public void onStartAndServiceRunning_showPreview() {
        presenter.start(false);
        // Show preview
        verify(view).initOpenCV(eq(presenter));
    }

    @Test
    public void methodsWithNoAction() {
        presenter.start();
        presenter.onCameraViewStopped();
    }

}
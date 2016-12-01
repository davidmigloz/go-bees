package com.davidmiguel.gobees.hive;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * nit tests for the implementation of HivePresenter.
 */
public class HivePresenterTest {

    private static final long HIVE_ID = 1;
    private static List<Recording> RECORDINGS;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private HiveContract.View hiveView;

    private HivePresenter hivePresenter;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetRecordingsCallback> getRecordingsCallbackArgumentCaptor;

    @Before
    public void setupHivesPresenter() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        hivePresenter = new HivePresenter(goBeesRepository, hiveView, HIVE_ID);

        // The presenter won't update the view unless it's active
        when(hiveView.isActive()).thenReturn(true);

        // Create a hive
        RECORDINGS = Lists.newArrayList(
                RecordingMother.newDefaultRecording(),
                RecordingMother.newDefaultRecording(),
                RecordingMother.newDefaultRecording());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loadRecordings_showRecordingsIntoView() {
        // Given an initialized HivePresenter
        // When loading of recordings is requested
        hivePresenter.loadRecordings(true);

        // Callback is captured and invoked with stubbed hives
        verify(goBeesRepository).getRecordings(anyLong(), getRecordingsCallbackArgumentCaptor.capture());
        getRecordingsCallbackArgumentCaptor.getValue().onRecordingsLoaded(RECORDINGS);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(hiveView);
        inOrder.verify(hiveView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(hiveView).setLoadingIndicator(false);
        ArgumentCaptor<List> showRecordingsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(hiveView).showRecordings(showRecordingsArgumentCaptor.capture());
        // Assert that the number of hives shown is the expected
        assertTrue(showRecordingsArgumentCaptor.getValue().size() == RECORDINGS.size());
    }
}
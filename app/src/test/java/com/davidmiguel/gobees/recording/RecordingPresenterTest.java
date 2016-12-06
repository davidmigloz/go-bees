package com.davidmiguel.gobees.recording;

import com.davidmiguel.gobees.data.model.Recording;
import com.davidmiguel.gobees.data.model.mothers.RecordingMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetRecordingCallback;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of RecordingPresenter.
 */
public class RecordingPresenterTest {

    private static final int HIVE_ID = 1;
    private static final Recording RECORDING = RecordingMother.newDefaultRecording();
    private static final Date DATE = new Date();

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private RecordingContract.View view;

    private RecordingPresenter presenter;

    @Captor
    private ArgumentCaptor<GetRecordingCallback> getRecordingCallbackArgumentCaptor;

    @Before
    public void setupHivesPresenter() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        presenter = new RecordingPresenter(goBeesRepository, view, HIVE_ID, DATE, DATE);

        // The presenter won't update the view unless it's active
        when(view.isActive()).thenReturn(true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void start_showRecordingIntoView() {
        // Given an initialized RecordingPresenter
        // When start is requested
        presenter.start();

        // Callback is captured and invoked with stubbed recording
        verify(goBeesRepository).getRecording(anyLong(), DATE, DATE,
                getRecordingCallbackArgumentCaptor.capture());
        getRecordingCallbackArgumentCaptor.getValue().onRecordingLoaded(RECORDING);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(view);
        inOrder.verify(view).setLoadingIndicator(true);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(view).setLoadingIndicator(false);
        ArgumentCaptor<Recording> showRecordingArgumentCaptor = ArgumentCaptor.forClass(Recording.class);
        verify(view).showRecording(showRecordingArgumentCaptor.capture());
        // Assert that the recording is correct
        assertTrue(showRecordingArgumentCaptor.getValue().getDate().equals(RECORDING.getDate()));
    }
}
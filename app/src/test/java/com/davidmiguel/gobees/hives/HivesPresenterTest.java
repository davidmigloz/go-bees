package com.davidmiguel.gobees.hives;

import com.davidmiguel.gobees.data.model.Hive;
import com.davidmiguel.gobees.data.model.HiveMother;
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
 * Unit tests for the implementation of HivesPresenter.
 */
public class HivesPresenterTest {

    private static final long APIARY_ID = 1;
    private static List<Hive> HIVES;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private HivesContract.View hivesView;

    private HivesPresenter hivesPresenter;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetHivesCallback> getHivesCallbackCaptor;

    @Before
    public void setupHivesPresenter() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        hivesPresenter = new HivesPresenter(goBeesRepository, hivesView, APIARY_ID);

        // The presenter won't update the view unless it's active
        when(hivesView.isActive()).thenReturn(true);

        // Create 3 hives
        HIVES = Lists.newArrayList(
                HiveMother.newDefaultHive(),
                HiveMother.newDefaultHive(),
                HiveMother.newDefaultHive());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loadAllApiariesFromRepositoryAndLoadIntoView() {
        // Given an initialized HivesPresenter with initialized apiaries
        // When loading of hives of apiary 1 is requested
        hivesPresenter.loadHives(true);

        // Callback is captured and invoked with stubbed hives
        verify(goBeesRepository).getHives(anyLong(), getHivesCallbackCaptor.capture());
        getHivesCallbackCaptor.getValue().onHivesLoaded(HIVES);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(hivesView);
        inOrder.verify(hivesView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(hivesView).setLoadingIndicator(false);
        ArgumentCaptor<List> showHivesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(hivesView).showHives(showHivesArgumentCaptor.capture());
        // Assert that the number of hives shown is the expected
        assertTrue(showHivesArgumentCaptor.getValue().size() == HIVES.size());
    }
}
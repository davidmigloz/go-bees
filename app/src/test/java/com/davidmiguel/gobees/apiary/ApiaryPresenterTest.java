package com.davidmiguel.gobees.apiary;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.mothers.ApiaryMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;

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
 * Unit tests for the implementation of ApiaryPresenter.
 */
public class ApiaryPresenterTest {

    private static final long APIARY_ID = 1;
    private static final int NUM_BEES = 5;
    private static Apiary APIARY;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private ApiaryContract.View hivesView;

    private ApiaryPresenter apiaryPresenter;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetHivesCallback> getHivesCallbackCaptor;

    @Captor
    private ArgumentCaptor<GoBeesDataSource.GetApiaryCallback> getApiaryCallbackArgumentCaptor;

    @Before
    public void setupHivesPresenter() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        apiaryPresenter = new ApiaryPresenter(goBeesRepository, hivesView, APIARY_ID);

        // The presenter won't update the view unless it's active
        when(hivesView.isActive()).thenReturn(true);

        // Create 3 hives
        APIARY = ApiaryMother.newDefaultApiary(NUM_BEES);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Test
    public void loadHives_showHivesIntoView() {
        // Given an initialized ApiaryPresenter
        // When loading of hives of apiary 1 is requested
        apiaryPresenter.loadHives(true);

        // Callback is captured and invoked with stubbed hives
        verify(goBeesRepository).getApiary(anyLong(), getApiaryCallbackArgumentCaptor.capture());
        getApiaryCallbackArgumentCaptor.getValue().onApiaryLoaded(APIARY);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(hivesView);
        inOrder.verify(hivesView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all hives are shown in UI
        inOrder.verify(hivesView).setLoadingIndicator(false);
        ArgumentCaptor<List> showHivesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(hivesView).showHives(showHivesArgumentCaptor.capture());
        // Assert that the number of hives shown is the expected
        assertTrue(showHivesArgumentCaptor.getValue().size() == APIARY.getHives().size());
    }
}
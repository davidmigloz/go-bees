package com.davidmiguel.gobees.apiaries;

import com.davidmiguel.gobees.data.model.Apiary;
import com.davidmiguel.gobees.data.model.ApiaryMother;
import com.davidmiguel.gobees.data.source.GoBeesDataSource.GetApiariesCallback;
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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of ApiariesPresenter.
 */
public class ApiariesPresenterTest {

    private static List<Apiary> APIARIES;

    @Mock
    private GoBeesRepository goBeesRepository;

    @Mock
    private ApiariesContract.View apiariesView;

    private ApiariesPresenter apiariesPresenter;

    @Captor
    private ArgumentCaptor<GetApiariesCallback> getApiariesCallbackCaptor;

    @Before
    public void setupApiariesPresenter() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        apiariesPresenter = new ApiariesPresenter(goBeesRepository, apiariesView);

        // The presenter won't update the view unless it's active
        when(apiariesView.isActive()).thenReturn(true);

        // Create 3 apiaries
        APIARIES = Lists.newArrayList(
                ApiaryMother.newDefaultApiary(),
                ApiaryMother.newDefaultApiary(),
                ApiaryMother.newDefaultApiary());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void loadAllApiariesFromRepositoryAndLoadIntoView() {
        // Given an initialized ApiariesPresenter with initialized apiaries
        // When loading of apiaries is requested
        apiariesPresenter.loadApiaries(true);

        // Callback is captured and invoked with stubbed apiaries
        verify(goBeesRepository).getApiaries(getApiariesCallbackCaptor.capture());
        getApiariesCallbackCaptor.getValue().onApiariesLoaded(APIARIES);

        // Then progress indicator is shown
        InOrder inOrder = inOrder(apiariesView);
        inOrder.verify(apiariesView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all apairies are shown in UI
        inOrder.verify(apiariesView).setLoadingIndicator(false);
        ArgumentCaptor<List> showApiariesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(apiariesView).showApiaries(showApiariesArgumentCaptor.capture());
        // Assert that the number of apairies shown is the expected
        assertTrue(showApiariesArgumentCaptor.getValue().size() == APIARIES.size());
    }
}
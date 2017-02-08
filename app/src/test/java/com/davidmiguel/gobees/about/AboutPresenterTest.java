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

package com.davidmiguel.gobees.about;

import com.davidmiguel.gobees.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of AboutPresenter.
 */
public class AboutPresenterTest {

    @Mock
    private AboutContract.View view;

    private AboutPresenter aboutPresenter;

    @Before
    public void setupMocksAndView() {
        // To inject the mocks in the test the initMocks method needs to be called
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        aboutPresenter = new AboutPresenter(view);

        // The presenter won't update the view unless it's active
        when(view.isActive()).thenReturn(true);
    }

    @Test
    public void start_showVersionAndLibraries() {
        aboutPresenter.start();
        // Verify version is shown
        verify(view).showVersion(eq(BuildConfig.VERSION_NAME));
        // Verify libraries are shown
        verify(view).showLibraries(anyListOf(Library.class));
    }

    @Test
    public void onWebsiteClicked_openWebsite() {
        aboutPresenter.onWebsiteClicked();
        // Verify that the website is opened
        verify(view).openWebsite(anyInt());
    }

    @Test
    public void onChangelogClicked_openChangelog() {
        aboutPresenter.onChangelogClicked();
        // Verify that the changelog is opened
        verify(view).openChangelog(anyInt(), anyInt());
    }

    @Test
    public void onLicenseClicked_openLicense() {
        // APACHE2
        aboutPresenter.onLicenseClicked(Library.License.APACHE2);
        verify(view).openLicence(eq(Library.License.APACHE2.toString()), anyInt());
        // GPL3
        aboutPresenter.onLicenseClicked(Library.License.GPL3);
        verify(view).openLicence(eq(Library.License.GPL3.toString()), anyInt());
        // BSD
        aboutPresenter.onLicenseClicked(Library.License.BSD);
        verify(view).openLicence(eq(Library.License.BSD.toString()), anyInt());
        // CCBYSA4
        aboutPresenter.onLicenseClicked(Library.License.CCBYSA4);
        verify(view).openLicence(eq(Library.License.CCBYSA4.toString()), anyInt());
        // MIT
        aboutPresenter.onLicenseClicked(Library.License.MIT);
        verify(view).openLicence(eq(Library.License.MIT.toString()), anyInt());
    }
}
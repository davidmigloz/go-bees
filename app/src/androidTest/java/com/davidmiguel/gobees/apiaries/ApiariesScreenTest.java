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

package com.davidmiguel.gobees.apiaries;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.davidmiguel.gobees.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests for the apiaries screen, the main screen which contains a list of all apiaries.
 */
@RunWith(AndroidJUnit4.class)
public class ApiariesScreenTest {

    /**
     * Launch your activity under test.
     */
    @Rule
    public ActivityTestRule<ApiariesActivity> apiariesActivityTestRule =
            new ActivityTestRule<>(ApiariesActivity.class);

    @Test
    public void clickAddApiaryButton_opensAddApiaryUi() {
        // Click on the add apiary button
        onView(withId(R.id.fab_add_apiary)).perform(click());
        // Check if the add apiary screen is displayed
        onView(withId(R.id.add_apiary_name)).check(matches(isDisplayed()));
    }

}
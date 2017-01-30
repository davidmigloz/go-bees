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

package com.davidmiguel.gobees.hive;


import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.splash.SplashActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Delete recording test.
 * Needs camera permissions.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteRecordingTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void deleteRecordingTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_add_apiary),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.add_apiary_name));
        appCompatEditText.perform(scrollTo(), replaceText("Apiary"), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab_add_apiary),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.apiaries_list),
                        withParent(allOf(withId(R.id.apiariesLL),
                                withParent(withId(R.id.apiariesContainer)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab_add_hive),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.add_hive_name));
        appCompatEditText2.perform(scrollTo(), replaceText("Hive"), closeSoftKeyboard());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.fab_add_hive),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.hives_list),
                        withParent(allOf(withId(R.id.hivesLL),
                                withParent(withId(R.id.hivesContainer)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction floatingActionButton5 = onView(
                allOf(withId(R.id.fab_new_recording),
                        withParent(withId(R.id.coordinatorLayout)),
                        isDisplayed()));
        floatingActionButton5.perform(click());

        ViewInteraction appCompatImageView = onView(
                withId(R.id.record_icon));
        appCompatImageView.perform(click());

        SystemClock.sleep(20000);

        ViewInteraction appCompatImageView2 = onView(
                withId(R.id.record_icon));
        appCompatImageView2.perform(click());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.more_icon), withContentDescription("More…"), isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Delete"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.no_recordings_text));
        textView.check(matches(isDisplayed()));
    }
}

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
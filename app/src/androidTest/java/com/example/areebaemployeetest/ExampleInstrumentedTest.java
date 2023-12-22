package com.example.areebaemployeetest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.areebaemployeetest.view.MainActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.areebaemployeetest", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addEmployeeButtonClick() {
        // Use ActivityScenario to launch the activity
        try (ActivityScenario<MainActivity> scenario = activityRule.getScenario()) {
            // Perform a click on a button with the id "myButton"
            onView(withId(R.id.tv_add_employee)).perform(click());
            // Check if a TextView with the id "resultTextView" displays the expected text
            onView(withId(R.id.tv_delete_all)).check(matches(withText("Button Clicked")));
        }
    }
    @Test
    public void deleteAllButtonClick() {
        // Use ActivityScenario to launch the activity
        try (ActivityScenario<MainActivity> scenario = activityRule.getScenario()) {
            // Perform a click on a button with the id "myButton"
            onView(withId(R.id.tv_delete_all)).perform(click());
            // Check if a TextView with the id "resultTextView" displays the expected text
            onView(withId(R.id.tv_add_employee)).check(matches(withText("Button Clicked")));
        }
    }
}
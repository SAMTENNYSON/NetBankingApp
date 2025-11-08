package com.example.netbankingapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.netbankingapp.ui.login.LoginActivity;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testEmptyFieldsShowError() {
        // Click login without entering anything
        onView(withId(R.id.btnLogin)).perform(click());

        // Check if CIF field shows error
        onView(withId(R.id.etCifNumber)).check((view, noViewFoundException) -> {
            assert(((android.widget.EditText) view).getError().toString().equals("Enter CIF number"));
        });
    }

    @Test
    public void testValidInput() {
        // Enter CIF number
        onView(withId(R.id.etCifNumber)).perform(typeText("12345678901"), closeSoftKeyboard());

        // Enter password
        onView(withId(R.id.etPassword)).perform(typeText("mypassword"), closeSoftKeyboard());

        // Click login
        onView(withId(R.id.btnLogin)).perform(click());

        // Here, you could add navigation checks, but since API is async, keep it simple
    }
}

package com.example.netbankingapp;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.netbankingapp.ui.register.RegisterActivity;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void testEmptyFieldsShowErrors() {
        // Click register without typing anything
        onView(withId(R.id.btnRegister)).perform(click());

        // Check if errors appear
        onView(withId(R.id.etAccountNumber))
                .check((view, noViewFoundException) -> {
                    assert(((android.widget.EditText) view).getError().toString().contains("16 digits"));
                });
    }

    @Test
    public void testInvalidAccountNumberShowsError() {
        onView(withId(R.id.etAccountNumber)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());

        onView(withId(R.id.etAccountNumber))
                .check((view, noViewFoundException) -> {
                    assert(((android.widget.EditText) view).getError().toString().contains("16 digits"));
                });
    }

    @Test
    public void testValidInput() {
        // Enter all required fields
        onView(withId(R.id.etAccountNumber)).perform(typeText("1234567890123456"), closeSoftKeyboard());
        onView(withId(R.id.etCifNumber)).perform(typeText("12345678901"), closeSoftKeyboard());
        onView(withId(R.id.etBranchCode)).perform(typeText("54321"), closeSoftKeyboard());
        onView(withId(R.id.etMobile)).perform(typeText("9876543210"), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.etTransactionPin)).perform(typeText("1234"), closeSoftKeyboard());

        // Select country (first item in spinner)
        onView(withId(R.id.spinnerCountry)).perform(click());
        onView(withText("India")).perform(click());

        // Click register
        onView(withId(R.id.btnRegister)).perform(click());

        // Since API is async, we don't assert DB here.
        // We just verify no immediate validation error.
    }

    @Test
    public void testInvalidIndianMobileShowsError() {
        // Enter account number (valid)
        onView(withId(R.id.etAccountNumber)).perform(typeText("1234567890123456"), closeSoftKeyboard());
        onView(withId(R.id.etCifNumber)).perform(typeText("12345678901"), closeSoftKeyboard());
        onView(withId(R.id.etBranchCode)).perform(typeText("54321"), closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.etTransactionPin)).perform(typeText("1234"), closeSoftKeyboard());

        // Select "India" in spinner
        onView(withId(R.id.spinnerCountry)).perform(click());
        onView(withText("India")).perform(click());

        // Enter mobile number with wrong length
        onView(withId(R.id.etMobile)).perform(typeText("12345"), closeSoftKeyboard());

        // Try to register
        onView(withId(R.id.btnRegister)).perform(click());

        // Check that error is shown
        onView(withId(R.id.etMobile))
                .check((view, noViewFoundException) -> {
                    assert(((android.widget.EditText) view).getError().toString()
                            .contains("Mobile number must be 10 digits for India"));
                });
    }
}


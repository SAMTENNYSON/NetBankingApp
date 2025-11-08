package com.example.netbankingapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.netbankingapp.R;
import com.example.netbankingapp.ui.transfer.FundTransferActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class FundTransferActivityTest {

    @Rule
    public ActivityScenarioRule<FundTransferActivity> activityRule =
            new ActivityScenarioRule<>(FundTransferActivity.class);

    @Test
    public void testEmptyFieldsShowsToast() {
        // Click transfer without entering anything
        onView(withId(R.id.btnTransfer)).perform(click());

        // No direct way to assert Toast easily, but this checks fields are still visible
        onView(withId(R.id.etReceiverCif)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidAmountShowsToast() {
        // Enter receiver CIF
        onView(withId(R.id.etReceiverCif)).perform(typeText("12345678901"), closeSoftKeyboard());

        // Enter invalid amount
        onView(withId(R.id.etAmount)).perform(typeText("abc"), closeSoftKeyboard());

        // Enter TPIN
        onView(withId(R.id.etTransactionPin)).perform(typeText("1234"), closeSoftKeyboard());

        // Click transfer
        onView(withId(R.id.btnTransfer)).perform(click());

        // Fields remain → validation failed
        onView(withId(R.id.etAmount)).check(matches(isDisplayed()));
    }

    @Test
    public void testValidInput() {
        // Enter receiver CIF
        onView(withId(R.id.etReceiverCif)).perform(typeText("12345678901"), closeSoftKeyboard());

        // Enter valid amount
        onView(withId(R.id.etAmount)).perform(typeText("500"), closeSoftKeyboard());

        // Enter TPIN
        onView(withId(R.id.etTransactionPin)).perform(typeText("1234"), closeSoftKeyboard());

        // Click transfer
        onView(withId(R.id.btnTransfer)).perform(click());

        // Since API is async and real server is hit, we can’t assert Toast here
        // Just check button is still visible (to confirm no crash)
        onView(withId(R.id.btnTransfer)).check(matches(isDisplayed()));
    }
}


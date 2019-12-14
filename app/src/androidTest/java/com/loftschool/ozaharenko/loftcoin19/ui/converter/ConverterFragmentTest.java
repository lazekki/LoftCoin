package com.loftschool.ozaharenko.loftcoin19.ui.converter;

//UI tests library:
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.loftschool.ozaharenko.loftcoin19.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ConverterFragmentTest {

    private FragmentScenario<ConverterFragment> scenario;

    private CountingIdlingResource toValueIdling;

    @Before
    public void setUp() throws Exception {

        scenario = FragmentScenario.launchInContainer(ConverterFragment.class,
                Bundle.EMPTY,
                R.style.AppTheme_NoActionBar,
                null);

        toValueIdling = new CountingIdlingResource("toValue");
    }

    @Test
    public void fromValueChangeMustChangeToValue() {
        //First we test that at the test start we have placeholders with 0.00 values:
        final ViewInteraction fromView = onView(withId(R.id.from));
        final ViewInteraction toView = onView(withId(R.id.to));

        fromView.check(matches(withHint(R.string.zero_decimal)));
        toView.check(matches(withHint(R.string.zero_decimal)));

        toValueIdling.increment();

        //Second we type new value into fromView field with soft keyboard:
        fromView.perform(typeText("123"), closeSoftKeyboard());

        //wait a bit because of network response time
        //not the best way to do it, but:
/*
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

        //other option to avoid asynchronous:
        //1) replace Dagger component in tests (google, should be a lot of info)
        //2) use:


        scenario.onFragment(action -> {
            action.viewModel.toValue().subscribe(text -> {
                toValueIdling.decrement();
            });
        });

        IdlingRegistry.getInstance().register(toValueIdling);

        //and check that toView field is not empty after changes in fromView field:
        toView.check(matches(not(withText(""))));

    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(toValueIdling);
    }
}
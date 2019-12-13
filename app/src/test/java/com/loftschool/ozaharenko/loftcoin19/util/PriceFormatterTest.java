package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepoStub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PriceFormatterTest {

    private TestCurrencyRepo currencies;

    private PriceFormatter formatter;

    @Before
    public void setUp() throws Exception {
        currencies = new TestCurrencyRepo();
        formatter = new PriceFormatter(currencies);
    }

    @Test
    public void format() {

    }

    @Test
    public void formatWithoutCurrencySymbol() {

    }

    private static class TestCurrencyRepo extends CurrencyRepoStub {

    }
}
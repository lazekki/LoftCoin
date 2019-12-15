package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.common.truth.Truth;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepoStub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class PriceFormatterTest {

    private TestCurrencyRepo currencies;

    private PriceFormatter formatter;

    private List<Locale> locales;

    @Before
    public void setUp() throws Exception {
        currencies = new TestCurrencyRepo();
        formatter = new PriceFormatter(currencies);
        locales = Arrays.asList(
            Locale.US,
            Locale.GERMANY,
            new Locale("ru", "RU")
        );
    }

    @Test
    public void format() {
        for (final Locale locale : locales) {
            currencies.currentLocale = locale;
            final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            //format object belongs to NumberFormat class and calls method format from NumberFormat class;
            final String expectedValue = format.format(1.23);
            //formatter object belongs to PriceFormatter class and tests method format from PriceFormatter class;
            final String actualValue = formatter.format(1.23);
            assertThat(actualValue).isEqualTo(expectedValue);

        }
    }

    @Test
    public void formatWithoutCurrencySymbol() {
        for (final Locale locale : locales) {
            currencies.currentLocale = locale;
            final NumberFormat format = NumberFormat.getNumberInstance(locale);
            //format object belongs to NumberFormat class and calls method format from NumberFormat class;
            final String expectedValue = format.format(1.23);
            //formatter object belongs to PriceFormatter class and tests method formatWithoutCurrencySymbol from PriceFormatter class;
            final String actualValue = formatter.formatWithoutCurrencySymbol(1.23);
            assertThat(actualValue).isEqualTo(expectedValue);
        }
    }

    private static class TestCurrencyRepo extends CurrencyRepoStub {
        Locale currentLocale;

        @NonNull
        @Override
        public Locale getLocale() {
            return Objects.requireNonNull(currentLocale, "locale is null");
        }
    }
}
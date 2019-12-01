package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.data.Currency;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PriceFormatter implements Formatter<Double> {

    private final CurrencyRepo currencies;

    @Inject
    public PriceFormatter(CurrencyRepo currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public String format(@NonNull Double value) {
        final Currency currency = currencies.getCurrency();
        if (Objects.equals(currency.code(), "EUR" )) {
            return format(Locale.GERMANY, value);
        } else if (Objects.equals(currency.code(), "RUB" )) {
            return format(new Locale("ru", "RU"), value);
        }
        return format(Locale.US, value);
    }

    private String format(Locale locale, double value) {
        return NumberFormat.getCurrencyInstance(locale).format(value);
    }
}

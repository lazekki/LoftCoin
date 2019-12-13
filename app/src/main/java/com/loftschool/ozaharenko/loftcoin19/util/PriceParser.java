package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class PriceParser implements Parser<String, Double> {

    private final CurrencyRepo currencies;

    @Inject
    PriceParser(CurrencyRepo currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public Double parse(@NonNull String value) {
        if (value.isEmpty()) return 0d;
        return parse(NumberFormat.getNumberInstance(currencies.getLocale()), value);
    }

    private Double parse(@NonNull NumberFormat format, @NonNull String value) {
        try {
            final Number number = format.parse(value);
            if (number != null) {
                return number.doubleValue();
            }
        } catch (ParseException e) {
            Timber.e(e);
        }
        return 0d;
    }

}
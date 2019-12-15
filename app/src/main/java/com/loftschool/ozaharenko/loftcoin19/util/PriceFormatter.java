package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

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
        return NumberFormat.getCurrencyInstance(currencies.getLocale()).format(value);
    }

    @NonNull
    public String formatWithoutCurrencySymbol(@NonNull Double value) {
        final NumberFormat format = NumberFormat.getNumberInstance(currencies.getLocale());
        final DecimalFormat decimalFormat = (DecimalFormat) format;
        final DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        decimalFormat.setDecimalFormatSymbols(symbols);
        return format.format(value);
    }

}

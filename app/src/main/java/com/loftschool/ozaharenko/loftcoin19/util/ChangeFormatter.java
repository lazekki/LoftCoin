package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChangeFormatter implements Formatter<Double> {

    @Inject
    public ChangeFormatter() {
    }

    @NonNull
    @Override
    public String format(@NonNull Double value) {
        return String.format(Locale.US, "%.2f%%", value);
    }
}

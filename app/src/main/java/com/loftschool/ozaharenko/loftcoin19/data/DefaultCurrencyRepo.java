package com.loftschool.ozaharenko.loftcoin19.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.prefs.Settings;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DefaultCurrencyRepo implements CurrencyRepo {

    private final Settings settings;
    private final Context context;

    @Inject DefaultCurrencyRepo(Context context, Settings settings) {
        this.context = context;
        this.settings = settings;
    }

    @NonNull
    @Override
    public List<Currency> availableCurrencies() {
        return Arrays.asList(
                Currency.create("$", context.getString(R.string.usd), "USD"),
                Currency.create("€", context.getString(R.string.eur), "EUR"),
                Currency.create("₽", context.getString(R.string.rub), "RUB")
        );
    }

    @NonNull
    @Override
    public Currency getCurrency() {
        final List<Currency> currencies = availableCurrencies();
        for (Currency currency : currencies) {
            if (Objects.equals(currency.code(), settings.getDefaultCurrencyCode())) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency.");
    }

    @Override
    public void setCurrency(@NonNull Currency currency) {
        settings.setDefaultCurrencyCode(currency.code());
    }
}

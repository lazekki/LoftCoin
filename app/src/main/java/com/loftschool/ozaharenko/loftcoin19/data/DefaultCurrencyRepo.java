package com.loftschool.ozaharenko.loftcoin19.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.prefs.Settings;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class DefaultCurrencyRepo implements CurrencyRepo {

    private static final String CURRENCY = "currency";

    private final Context context;

    private final SharedPreferences currencies;

    @Inject DefaultCurrencyRepo(Context context) {
        this.context = context;
        currencies = context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
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
        final String selectedCurrency = currencies.getString(CURRENCY, "USD");
        for (Currency currency : availableCurrencies()) {
            if (Objects.equals(currency.code(), selectedCurrency)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency.");
    }

    @Override
    public void setCurrency(@NonNull Currency currency) {
        currencies.edit().putString(CURRENCY, currency.code()).apply();
        //settings.setDefaultCurrencyCode(currency.code());
    }

    @NonNull
    @Override
    public LiveData<Currency> currency() {
        return new MutableLiveData<Currency>() {
            @Override
            protected void onActive() {
            }

            @Override
            protected void onInactive() {
            }
        };
    }
}

package com.loftschool.ozaharenko.loftcoin19;

import android.content.Context;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.prefs.Settings;
import com.loftschool.ozaharenko.loftcoin19.util.ChangeFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;

public interface BaseComponent {

    static BaseComponent get(@NonNull Context context) {
        if (context.getApplicationContext() instanceof LoftApp) {
            return ((LoftApp)context.getApplicationContext()).getComponent();
        }
        throw new IllegalArgumentException("No such component in" + context);
    }

    Context context();

    Settings settings();

    CurrencyRepo currencyRepo();

    CoinsRepo coinsRepo();

    PriceFormatter priceFormatter();

    ChangeFormatter changeFormatter();
}

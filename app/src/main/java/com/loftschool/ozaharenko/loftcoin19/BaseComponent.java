package com.loftschool.ozaharenko.loftcoin19;

import android.content.Context;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.data.CmcApi;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.prefs.Settings;

public interface BaseComponent {

    static BaseComponent get(@NonNull Context context) {
        if (context.getApplicationContext() instanceof LoftApp) {
            return ((LoftApp)context.getApplicationContext()).getComponent();
        }
        throw new IllegalArgumentException("No such component in" + context);
    }

    Context context();

    Settings settings();

    CmcApi cmcApi();

    CurrencyRepo currencyRepo();
}

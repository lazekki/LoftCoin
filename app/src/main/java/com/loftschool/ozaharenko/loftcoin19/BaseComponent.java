package com.loftschool.ozaharenko.loftcoin19;

import android.content.Context;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.data.WalletsRepo;
import com.loftschool.ozaharenko.loftcoin19.prefs.Settings;
import com.loftschool.ozaharenko.loftcoin19.util.ChangeFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.ImageLoader;
import com.loftschool.ozaharenko.loftcoin19.util.LogoUrlFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceParser;

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

    WalletsRepo walletsRepo();

    PriceParser priceParser();

    PriceFormatter priceFormatter();

    ChangeFormatter changeFormatter();

    LogoUrlFormatter logoUrlFormatter();

    ImageLoader imageLoader();

}

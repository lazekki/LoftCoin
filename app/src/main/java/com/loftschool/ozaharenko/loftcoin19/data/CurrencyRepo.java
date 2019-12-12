package com.loftschool.ozaharenko.loftcoin19.data;

import io.reactivex.Observable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;

public interface CurrencyRepo {

    @NonNull
    List<Currency> availableCurrencies();

    @NonNull
    Currency getCurrency();

    @NonNull
    Locale getLocale();

    void setCurrency(@NonNull Currency currency);

    @NonNull
    Observable<Currency> currency();

}

package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public interface CurrencyRepo {

    @NonNull
    List<Currency> availableCurrencies();

    @NonNull Currency getCurrency();

    void setCurrency(@NonNull Currency currency);

    @NonNull LiveData<Currency> currency();

}

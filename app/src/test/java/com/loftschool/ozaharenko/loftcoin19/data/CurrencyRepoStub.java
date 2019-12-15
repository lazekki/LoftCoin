package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

public class CurrencyRepoStub implements CurrencyRepo {


    @NonNull
    @Override
    public List<Currency> availableCurrencies() {
       throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Currency getCurrency() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrency(@NonNull Currency currency) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Observable<Currency> currency() {
        throw new UnsupportedOperationException();
    }
}

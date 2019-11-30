package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class RatesViewModel extends ViewModel {

    private final MutableLiveData<List<Coin>> coins = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private final AtomicBoolean initialized = new AtomicBoolean();

    @Inject
    CoinsRepo coinsRepo; // component -> base(app)Component -> DataModule -> cmcApi()

    @Inject
    CurrencyRepo currencyRepo;

    @NonNull
    public AtomicBoolean isInitialized() {
        return initialized;
    }

    @NonNull
    LiveData<List<Coin>> getCoins() {
        return coins;
    }

    @NonNull
    LiveData<Boolean> isLoading() {
        return loading;
    }

    final void refresh() {
        coinsRepo.listings(coins, loading);
    }

    @Override
    protected void onCleared() {

    }
}

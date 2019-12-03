package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.Currency;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;


public class RatesViewModel extends ViewModel {

    private final MutableLiveData<List<Coin>> coins = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private final AtomicBoolean initialized = new AtomicBoolean();
    private final CoinsRepo coinsRepo;
    private final CurrencyRepo currencyRepo;

    private Observer<Currency> currencyObserver;

    //On @Inject there is call stack: component -> base(app)Component -> DataModule -> cmcApi():

    @Inject
    RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo) {
        this.coinsRepo = coinsRepo;
        this.currencyRepo = currencyRepo;
        observeCurrencyChange();
    }

    @NonNull
    LiveData<List<Coin>> getCoins() {

        return coins;
    }

    @NonNull
    LiveData<Boolean> isLoading() {

        return loading;
    }

    private void observeCurrencyChange() {
        if (currencyObserver == null) {
            currencyObserver = currency -> refresh();
            currencyRepo.currency().observeForever(currencyObserver);
        }
    }

    final void refresh() {

        coinsRepo.listings(coins, loading, currencyRepo.getCurrency());
    }

    @Override
    protected void onCleared() {
        if(currencyObserver != null) {
            currencyRepo.currency().removeObserver(currencyObserver);
        }
    }
}

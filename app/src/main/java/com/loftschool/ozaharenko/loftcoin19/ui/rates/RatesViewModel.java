package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.Currency;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

class RatesViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private final AtomicBoolean initialized = new AtomicBoolean();

    private final LiveData<List<Coin>> coins;

    private final MutableLiveData<Boolean> pullToRefresh = new MutableLiveData<>();

    //On @Inject there is call stack: component -> base(app)Component -> DataModule -> cmcApi():
    @Inject
    RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo) {

        /*'источник истины' :
        PullToRefresh -> [*]Сurrency -> [*]List<? extends Coin> -> ][*]List<Coin>
           все эти элементы, каждый по своему, служат источником истины, и могут быть объединены в цепочку.
        to implement it:
        */

        //Для PullToRefresh по refresh (см. method refresh(), line 71) публикуем значение и это значение триггерит начало цепочки.
        //После изменения валюты мы лезем в репозиторий и получаем список койнов.
        //Последним шагом кастуем список койнов к типу, который принимается адаптером.

        //PullToRefresh -> [*]Сurrency
        LiveData<Currency> currency = Transformations.switchMap(pullToRefresh, r -> currencyRepo.currency());

        //[*]Сurrency -> [*]List<? extends Coin>
        final LiveData<? extends List<? extends Coin>> rawCoins = Transformations
                .switchMap(currency, c -> {
                    loading.postValue(true);
                    return coinsRepo.listings(c, () -> {
                        loading.postValue(false);
                    });
                });

        //[*]List<? extends Coin> -> ][*]List<Coin>
        coins = Transformations.map(rawCoins, Collections::unmodifiableList);
        refresh();
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
          pullToRefresh.setValue(true);
    }

}

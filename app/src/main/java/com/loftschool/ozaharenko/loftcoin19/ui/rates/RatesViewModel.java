package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentRatesBinding;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class RatesViewModel extends ViewModel {

    private final Subject<AtomicBoolean> pullToRefresh = BehaviorSubject
            .createDefault(new AtomicBoolean(true));

    private final Subject<CoinsRepo.SortBy> sortBy = BehaviorSubject
            .createDefault(CoinsRepo.SortBy.RANK);

    private final Subject<Boolean> loading = BehaviorSubject.create();

    private final Queue<CoinsRepo.SortBy> sortByQueue = new ArrayDeque<>();

    private final Observable<List<Coin>> coins;

    // Pull2Refresh -> [*]Currency -> [*]List<? extends Coin> -> [*]List<Coin>
    @Inject
    RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo) {

        // Upstream
        // Downstream
        coins = pullToRefresh //Upstream for all rest chain:
                .observeOn(Schedulers.io())

                //Downstream
                .switchMap(refresh -> currencyRepo.currency()
                        .observeOn(Schedulers.io())
                        .doOnNext(c -> refresh.set(true))
                        .doOnNext(c -> loading.onNext(true))
                        .switchMap(currency -> sortBy
                                .observeOn(Schedulers.io())
                                .map(sortingOrder -> CoinsRepo.Query.create(
                                        currency,
                                        sortingOrder,
                                        refresh.getAndSet(false)
                                ))
                        )
                )
                .switchMap(coinsRepo::listings)
                .<List<Coin>>map(Collections::unmodifiableList)
                .doOnNext(c -> loading.onNext(false))
                .doOnError(e -> loading.onNext(false))
                .replay(1)
                .autoConnect()

                //Upstream:
                .subscribeOn(Schedulers.io());

        Collections.addAll(sortByQueue,
                CoinsRepo.SortBy.RANK,
                CoinsRepo.SortBy.PRICE_DESC,
                CoinsRepo.SortBy.PRICE_ASC
        );

    }

    @NonNull
    Observable<List<Coin>> getCoins() {
        return coins.observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Observable<Boolean> isLoading() {
        return loading.observeOn(AndroidSchedulers.mainThread());
    }

    final void refresh() {
        pullToRefresh.onNext(new AtomicBoolean(true));
    }

    final void selectNextSortingOrder() {
        // RANK -> PRICE_DESC -> PRICE_ASC
        final CoinsRepo.SortBy sort = sortByQueue.poll(); // PRICE_DESC -> PRICE_ASC
        sortBy.onNext(sort);
        sortByQueue.offer(sort); // PRICE_DESC -> PRICE_ASC -> RANK
    }

}

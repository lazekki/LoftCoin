package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
class CmcCoinsRepo implements CoinsRepo {

    private final CmcApi api;

    private final CoinsDb db;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject CmcCoinsRepo(CmcApi api, CoinsDb db) {
        this.api = api;
        this.db = db;
    }

    @NonNull
    @Override
    public Observable<? extends List<? extends Coin>> listings(@NonNull Query query) {
        return Observable
                .defer(() -> query.forceUpdate() ? fetchFromNetwork(query) : Observable.empty())
                .switchMap(Observable::fromIterable)
                .map(cmcCoin -> RoomCoin.create(
                        cmcCoin.id(),
                        cmcCoin.name(),
                        cmcCoin.symbol(),
                        cmcCoin.price(),
                        cmcCoin.change24h()
                ))
                .toList().toObservable()
                .doOnNext(db.coins()::insertAll)
                .switchMap(coins -> fetchFromDb(query))
                .switchIfEmpty(coins -> fetchFromDb(query));    }

    @NonNull
    private Observable<? extends List<? extends Coin>> fetchFromDb(@NonNull Query query) {
        switch (query.sortBy()) {
            case PRICE_ASC:
                return db.coins().fetchAllSortedByPriceAsc();
            case PRICE_DESC:
                return db.coins().fetchAllSortedByPriceDesc();
            default:
                return db.coins().fetchAll();
        }
    }

    @NonNull
    private Observable<? extends List<? extends Coin>> fetchFromNetwork(@NonNull Query query) {
        return api
                .listings(query.currency().code())
                .map(Listings::data);
    }


}

package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
class CmcCoinsRepo implements CoinsRepo {

    private final CmcApi api;

    private final CoinsDb db;

    @Inject
    CmcCoinsRepo(CmcApi api, CoinsDb db) {
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
                        cmcCoin.change24h(),
                        cmcCoin.rank()
                ))
                .toList().toObservable()
                .doOnNext(db.coins()::insertAll)
                .switchMap(coins -> fetchFromDb(query))
                .switchIfEmpty(coins -> fetchFromDb(query));
    }


    @NonNull
    @Override
    public Observable<? extends List<? extends Coin>> top(@NonNull Currency currency, int limit) {
        return db.coins()
                .fetchAllSortedByRank(limit)
                .switchMap(coins -> {
                    if (coins.isEmpty()) {
                        return listings(Query.create(currency, SortBy.RANK, true, limit));
                    } else {
                        return Observable.just(coins);
                    }
                });
    }

    @NonNull
    @Override
    public Single<? extends Coin> coin(@NonNull Currency currency, Long id) {
        return db.coins().fetchOne(id)
                .onErrorResumeNext(e -> listings(Query
                        .create(currency, SortBy.RANK, true))
                        .switchMapSingle(coins -> db.coins().fetchOne(id))
                        .firstOrError()
                );
    }

    @NonNull
    @Override
    public Single<? extends Coin> nextWithIdNotIn(@NonNull Currency currency, @NonNull List<Long> ids) {
        return db.coins()
                .nextCoinWithIdNotIn(ids)
                .onErrorResumeNext(e -> listings(Query
                        .create(currency, SortBy.RANK, true))
                        .switchMapSingle(coins -> db.coins().nextCoinWithIdNotIn(ids))
                        .firstOrError()
                );
    }

    @NonNull
    private Observable<? extends List<? extends Coin>> fetchFromDb(@NonNull Query query) {
        switch (query.sortBy()) {
            case PRICE_ASC:
                return db.coins().fetchAllSortedByPriceAsc(query.limit());
            case PRICE_DESC:
                return db.coins().fetchAllSortedByPriceDesc(query.limit());
            default:
                return db.coins().fetchAllSortedByRank(query.limit());
        }
    }

    @NonNull
    private Observable<? extends List<? extends Coin>> fetchFromNetwork(@NonNull Query query) {
        return api
                .listings(query.currency().code())
                .map(Listings::data);
    }
}

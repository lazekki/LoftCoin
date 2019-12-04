package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
class CmcCoinsRepo implements CoinsRepo {

    private final CmcApi api;

    private final CoinsDb db;

    private LiveData<List<RoomCoin>> coins;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    CmcCoinsRepo(CmcApi api, CoinsDb db) {
        this.api = api;
        this.db = db;
        coins = db.coins().fetchAll();
    }

    @NonNull
    @Override
    public LiveData<? extends List<? extends Coin>> listings(
        @NonNull Currency currency,
        @NonNull Runnable onComplete) {
        executor.execute(() -> { //execute request to update data not in main thread
            try {
                final Response<Listings> response = api.listings(currency.code()).execute();
                if (response.isSuccessful() && response.body() != null) {
                    final List<AutoValue_CmcCoin> cmcCoins = response.body().data();
                    final List<RoomCoin> roomCoins = new ArrayList<>(cmcCoins.size());
                    for (AutoValue_CmcCoin cmcCoin : cmcCoins) {
                        roomCoins.add(RoomCoin.create(
                                cmcCoin.id(),
                                cmcCoin.name(),
                                cmcCoin.symbol(),
                                cmcCoin.price(),
                                cmcCoin.change24h()
                        ));
                    }

                    db.coins().insertAll(roomCoins); //insert new set of data
                }
            } catch (IOException e) {
                Timber.e(e);
            }
            onComplete.run();              //end of request to update data
        });
        return coins;
    }
}

package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

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

    @Inject
    CmcCoinsRepo(CmcApi api, CoinsDb db) {
        this.api = api;
        this.db = db;
    }

    public void listings(@NonNull MutableLiveData<List<Coin>> coins,
                         @NonNull MutableLiveData<Boolean> loading,
                         @NonNull Currency currency) {
        loading.setValue(true);
        api.listings(currency.code()).enqueue(new Callback<Listings>() {
            @Override
            public void onResponse(Call<Listings> call, Response<Listings> response) {
                loading.postValue(false);
                final Listings listings = response.body();
                if (listings != null) {
                    coins.postValue(listings.coins());
                }
            }

            @Override
            public void onFailure(Call<Listings> call, Throwable t) {
                loading.postValue(false);
                Timber.d(t);
            }
        });
    }
}

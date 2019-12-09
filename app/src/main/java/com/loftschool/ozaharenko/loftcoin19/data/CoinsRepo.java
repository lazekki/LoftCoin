package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.auto.value.AutoValue;

import java.util.List;

import io.reactivex.Observable;

public interface CoinsRepo {

    @NonNull
    Observable<? extends List<? extends Coin>> listings (
            @NonNull Query query
    );

    enum SortBy {
        RANK,
        PRICE_ASC,
        PRICE_DESC
    }

    @AutoValue
    abstract class Query {

        @NonNull
        public static Query create(@NonNull Currency currency, SortBy sortBy, boolean forceUpdate) {
            return new AutoValue_CoinsRepo_Query(currency, sortBy, forceUpdate);
        }

        public abstract Currency currency();

        public abstract SortBy sortBy();

        public abstract boolean forceUpdate();

    }


}

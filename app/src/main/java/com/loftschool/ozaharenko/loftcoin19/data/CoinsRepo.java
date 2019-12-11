package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface CoinsRepo {

    @NonNull
    Observable<? extends List<? extends Coin>> listings (
            @NonNull Query query
    );

    @NonNull
    Observable<? extends List<? extends Coin>> top(@NonNull Currency currency, int count);

    @NonNull
    Single<? extends Coin> coin(@NonNull Currency currency, Long id);

    @NonNull
    Single<? extends Coin> nextWithIdNotIn(@NonNull Currency currency, @NonNull List<Long> ids);

    enum SortBy {
        RANK,
        PRICE_ASC,
        PRICE_DESC
    }

    @AutoValue
    abstract class Query {

        @NonNull
        public static Query create(@NonNull Currency currency, SortBy sortBy, boolean forceUpdate) {
            return create(currency, sortBy, forceUpdate, Integer.MAX_VALUE);
        }

        @NonNull
        public static Query create(@NonNull Currency currency, SortBy sortBy, boolean forceUpdate, int limit) {
            return new AutoValue_CoinsRepo_Query(currency, sortBy, forceUpdate, limit);
        }

        public abstract Currency currency();

        public abstract SortBy sortBy();

        public abstract boolean forceUpdate();

        public abstract int limit();

    }


}

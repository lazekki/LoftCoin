package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class CoinsRepoStub implements CoinsRepo {

    @NonNull
    @Override
    public Observable<? extends List<? extends Coin>> listings(@NonNull Query query) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Observable<? extends List<? extends Coin>> top(@NonNull Currency currency, int count) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Single<? extends Coin> coin(@NonNull Currency currency, Long id) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Single<? extends Coin> nextWithIdNotIn(@NonNull Currency currency, @NonNull List<Long> ids) {
        throw new UnsupportedOperationException();
    }
}

package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface CoinsRepo {

    @NonNull
    LiveData<? extends List<?extends Coin>> listings(
            @NonNull Currency currency,
            @NonNull Runnable onComplete
    );

}

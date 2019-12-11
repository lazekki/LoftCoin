package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;

public interface Parser<T, R> {

    @NonNull
    R parse(@NonNull T value);

}

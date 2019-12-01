package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;

public interface Formatter<T> {
    @NonNull String format(@NonNull T value);
}

package com.loftschool.ozaharenko.loftcoin19.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.NoSuchElementException;

@AutoValue
public abstract class Optional<T> {

    private static final Optional<?> EMPTY = of(null);

    @NonNull
    public static <T> Optional<T> of(@Nullable T value) {
        return new AutoValue_Optional<>(value);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    @Nullable
    abstract T value();

    public boolean isPresent() {
        return value() != null;
    }

    @NonNull
    public T get() {
        if (value() == null) {
            throw new NoSuchElementException("No value present");
        }
        return value();
    }

}

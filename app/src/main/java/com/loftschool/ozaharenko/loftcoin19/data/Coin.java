package com.loftschool.ozaharenko.loftcoin19.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;

import java.util.Iterator;
import java.util.Map;

@AutoValue
public abstract class Coin {

    public abstract long id();

    public abstract String name();

    public abstract String symbol();

    abstract Map<String, AutoValue_Coin_Quote> quote();

    public double price() {
        final Iterator<AutoValue_Coin_Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) {
            return iterator.next().price();
        }
        return 0d;
    }

    public double change24h() {
        final Iterator<AutoValue_Coin_Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) {
            return iterator.next().change24h();
        }
        return 0d;
    }

    @AutoValue
    abstract static class Quote {

        public abstract double price();

        @AutoValue.CopyAnnotations
        @Json(name = "percent_change_24h")
        public abstract double change24h();

    }

}

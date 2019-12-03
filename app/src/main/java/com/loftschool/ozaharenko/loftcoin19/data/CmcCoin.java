package com.loftschool.ozaharenko.loftcoin19.data;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;

import java.util.Iterator;
import java.util.Map;

@AutoValue
abstract class CmcCoin implements Coin {

    @Override
    public double price() {
        final Iterator<? extends Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) {
            return iterator.next().price();
        }
        return 0d;
    }

    @Override
    public double change24h() {
        final Iterator<? extends Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) {
            return iterator.next().change24h();
        }
        return 0d;
    }

    abstract Map<String, AutoValue_CmcCoin_Quote> quote();

    @AutoValue
    abstract static class Quote {

        public abstract double price();

        @AutoValue.CopyAnnotations
        @Json(name = "percent_change_24h")
        public abstract double change24h();

    }
}

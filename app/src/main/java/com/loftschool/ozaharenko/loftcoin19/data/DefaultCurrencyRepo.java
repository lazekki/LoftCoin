package com.loftschool.ozaharenko.loftcoin19.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.loftschool.ozaharenko.loftcoin19.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

@Singleton
class DefaultCurrencyRepo implements CurrencyRepo {

    private static final Map<String, Locale> LOCALES = new HashMap<>();

    static {
        LOCALES.put("USD", Locale.US);
        LOCALES.put("EUR", Locale.GERMANY);
        LOCALES.put("RUB", new Locale("ru", "RU"));
    }

    private static final String CURRENCY = "currency";

    private final Context context;

    private final SharedPreferences currencies;

    @Inject
    DefaultCurrencyRepo(Context context) {
        this.context = context;
        currencies = context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public List<Currency> availableCurrencies() {
        return Arrays.asList(
                Currency.create("$", context.getString(R.string.usd), "USD"),
                Currency.create("€", context.getString(R.string.eur), "EUR"),
                Currency.create("₽", context.getString(R.string.rub), "RUB")
        );
    }

    @NonNull
    @Override
    public Observable<Currency> currency() {
        return Observable.create(new RxCurrency(this));
    }

    @Override
    public void setCurrency(@NonNull Currency currency) {
        currencies.edit().putString(CURRENCY, currency.code()).apply();
    }

    @NonNull
    @Override
    public Currency getCurrency() {
        final String selectedCurrency = currencies.getString(CURRENCY, "USD");
        for (Currency currency : availableCurrencies()) {
            if (Objects.equals(currency.code(), selectedCurrency)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency");
    }

    @NonNull
    @Override
    public Locale getLocale() {
        final Locale locale = LOCALES.get(getCurrency().code());
        return locale == null ? Locale.US : locale;
    }

    private static class RxCurrency implements ObservableOnSubscribe<Currency> {

        private final DefaultCurrencyRepo repo;

        RxCurrency(DefaultCurrencyRepo repo) {
            this.repo = repo;
        }

        @Override
        public void subscribe(ObservableEmitter<Currency> emitter) throws Exception {
            final SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> {
                emitter.onNext(repo.getCurrency());
            };
            emitter.setCancellable(() -> repo.currencies.unregisterOnSharedPreferenceChangeListener(listener));
            repo.currencies.registerOnSharedPreferenceChangeListener(listener);
            emitter.onNext(repo.getCurrency());
        }

    }

    private static class CurrencyLiveData extends MutableLiveData<Currency>
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final DefaultCurrencyRepo repo;

        CurrencyLiveData(DefaultCurrencyRepo repo) {
            this.repo = repo;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            postValue(repo.getCurrency());
        }

        @Override
        protected void onActive() {
            repo.currencies.registerOnSharedPreferenceChangeListener(this);
            postValue(repo.getCurrency());
        }

        @Override
        protected void onInactive() {
            repo.currencies.unregisterOnSharedPreferenceChangeListener(this);
        }

    }

}

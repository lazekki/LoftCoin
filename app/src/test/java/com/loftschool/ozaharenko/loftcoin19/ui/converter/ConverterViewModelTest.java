package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.data.CoinStub;
import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepoStub;
import com.loftschool.ozaharenko.loftcoin19.data.Currency;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepoStub;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


@RunWith(AndroidJUnit4.class)
public class ConverterViewModelTest {

    private TestCurrencyRepo currencies;

    private TestCoinsRepo coinsRepo;

    private PriceFormatter priceFormatter;

    private ConverterViewModel viewModel;

    //Test data:
    private Map<Currency, List<Coin>> coins;


    @Before
    public void setUp() throws Exception {

        //control for Schedulers within test, required to avoid switches between threads:
        RxAndroidPlugins.setMainThreadSchedulerHandler(s -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(s -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(s -> Schedulers.trampoline());

        //Test data:
        coins = new HashMap<>();
        coins.put(Currency.create("$", "USD", "USD"), Arrays.asList(
            CoinStub.create(1, "BTC", 100),
            CoinStub.create(2, "ETH", 10),
            CoinStub.create(3, "XRP", 1)
        ));

        coins.put(Currency.create("₽", "RUB", "RUB"), Arrays.asList(
            CoinStub.create(1, "BTC", 500),
            CoinStub.create(2, "ETH", 50),
            CoinStub.create(3, "XRP", 5)
        ));

        currencies = new TestCurrencyRepo();
        coinsRepo = new TestCoinsRepo(coins);
        priceFormatter = new PriceFormatter(currencies);
        viewModel = new ConverterViewModel(
                coinsRepo,
                currencies,
                priceFormatter,
                new PriceParser(currencies)
        );
    }

    @Test
    public void topCoinsMustReactOnCurrencyChange() {
        for (final Map.Entry<Currency, List<Coin>> entry : coins.entrySet()) {
            currencies.currency.onNext(entry.getKey());

            //test() - RxJava method to test:
            viewModel.topCoins().test().
                    awaitCount(1)                        //awaiting at least 1 value to test
                    .assertValue(entry.getValue());
        }
    }

    @Test
    public void fromCoinMustReactOnPositionChange() {
        for (final Map.Entry<Currency, List<Coin>> entry : coins.entrySet()) {
            currencies.currency.onNext(entry.getKey());
            for (int i = 0; i < entry.getValue().size(); ++i) {
                viewModel.fromCoin(i);
                viewModel.fromCoin()
                    .firstOrError()
                    .test()
                    .awaitCount(1)
                    .assertValue(entry.getValue().get(i));
            }
        }
    }


    @Test
    public void toValueMustReactOnFromValueChange() {
        for (final Map.Entry<Currency, List<Coin>> entry : coins.entrySet()) {
            currencies.currency.onNext(entry.getKey());

            final Coin fromCoin = entry.getValue().get(0);
            final Coin toCoin = entry.getValue().get(1);
            final double factor = fromCoin.price() / toCoin.price();

            //next row imitate the case when user enters 1.23, 5, 10 numbers into converter fields:
            for (double fromValue : new double[]{1.23, 5, 10}) {
                viewModel.fromValue(priceFormatter
                    .formatWithoutCurrencySymbol(fromValue)
                );

                //test() - RxJava method to test:
                viewModel.toValue().test()
                    .awaitCount(1)                        //awaiting at least 1 value to test
                    .assertValue(priceFormatter.formatWithoutCurrencySymbol( fromValue * factor)
                );
            }
        }
    }

    private static class TestCurrencyRepo extends CurrencyRepoStub {

        final Subject<Currency> currency = BehaviorSubject.create();

        @NonNull
        @Override
        public Observable<Currency> currency() {
            return currency;
        }

        @NonNull
        @Override
        public Locale getLocale() {
            return Locale.US;
        }
    }

    @After
    public void tearDown() throws Exception {
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

    private static class TestCoinsRepo extends CoinsRepoStub {

        private final Map<Currency, List<Coin>> coins;

        public TestCoinsRepo(Map<Currency, List<Coin>> coins) {
            this.coins = coins;
        }

        @NonNull
        @Override
        public Observable<? extends List<? extends Coin>> top(@NonNull Currency currency, int count) {
            return Observable.fromCallable(() -> coins.get(currency));
        }
    }
}
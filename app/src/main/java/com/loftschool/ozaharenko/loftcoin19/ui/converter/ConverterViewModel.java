package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.data.CoinsRepo;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceParser;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

//from: [amount, coin]
//to:   [amount, coin]

class ConverterViewModel extends ViewModel {

    //next 4 Subjects - come from UI, screen Converter.
    //these 2 Subjects we select a coins from list:
    private final Subject<Integer> fromPosition = BehaviorSubject.createDefault(0); // selected POSITION in adapter

    private final Subject<Integer> toPosition = BehaviorSubject.createDefault(1);   // selected POSITION in adapter

    //these 2 Subjects we enter as a numbers into fields:
    private final Subject<String> fromValue = BehaviorSubject.create(); // text in FROM field

    private final Subject<String> toValue = BehaviorSubject.create();   // text in TO field

    private final Observable<List<Coin>> topCoins;

    private final Observable<Coin> fromCoin;

    private final Observable<Coin> toCoin;

    private final Observable<Double> factor;

    private final PriceFormatter priceFormatter;

    private final PriceParser priceParser;

    @Inject
    ConverterViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo,
                       PriceFormatter priceFormatter, PriceParser priceParser) {
        this.priceFormatter = priceFormatter;
        this.priceParser = priceParser;

        //from [*] -> currency -> [*]coins -> [*]position -> [*]coin
        //to   [*] -> currency -> [*]coins -> [*]position -> [*]coin

        //distinct:             return 1 2 3 4 from 1 1 2 3 2 3 4
        //distinctUntilChanged: return 1 from 1 1 1
        //distinctUntilChanged: return 1 2 from 1 1 2
        //distinctUntilChanged: return 1 2 1 from 1 2 1
        //пока в цепочке чисел идут одинаковые, срабатывает distinct.
        //как только появляется новое число, случается событие untilChanged, и это число попадает в цепочку как новое.
        //если следующее число будет снова другим, не таким как предыдущее, это снова триггерит событие untilChanged, и это число так же
        //попадает в итоговую цепочку как новое. По факту, distinctUntilChanged фильтрует цепочку, убирая повторяющиеся
        //значения из подцепочек одинаковых элементов.

        //from [*] -> currency -> [*]coins
        topCoins = currencyRepo.currency()
                .switchMap(currency -> coinsRepo.top(currency, 3))
                .<List<Coin>>map(Collections::unmodifiableList)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect()
                .subscribeOn(Schedulers.io());

        //from [*]coins -> [*]position -> [*]coin
        fromCoin = topCoins
                .switchMap(coins -> fromPosition
                        .observeOn(Schedulers.computation())
                        .distinctUntilChanged()
                        .map(coins::get))
                .replay(1)
                .autoConnect()
                .subscribeOn(Schedulers.computation());

        //to [*]coins -> [*]position -> [*]coin
        toCoin = topCoins
                .switchMap(coins -> toPosition
                        .observeOn(Schedulers.computation())
                        .distinctUntilChanged()
                        .map(coins::get))
                .replay(1)
                .autoConnect()
                .subscribeOn(Schedulers.computation());

        //multiplier to calculate
        //[*]fromCoin -> [*]toCoin -> [*]factor
        //[*]currency -> [*]coins -> [*]position -> [*]coin -> [*]factor
        factor = fromCoin
                //.observeOn(Schedulers.computation())
                .switchMap(fc -> toCoin
                        //.observeOn(Schedulers.computation())
                        .map(tc -> fc.price() / tc.price())
                )
                .replay(1)
                .autoConnect()
                .subscribeOn(Schedulers.computation());
    }

    @NonNull
    Observable<List<Coin>> topCoins() {

        return topCoins.observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Observable<Coin> fromCoin() {

        return fromCoin.observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Observable<Coin> toCoin() {

        return toCoin.observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Observable<String> fromValue() {
        //[*]currency -> [*]coins -> [*]position -> [*]coin -> [*]factor -> [*]toValue -> fromValue
        return toValue
                .compose(parseValue())
                .switchMap(value -> factor.map(f -> value / f))
                .compose(formatValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    Observable<String> toValue() {
        //[*]currency -> [*]coins -> [*]position -> [*]coin -> [*]factor -> [*]fromValue -> toValue
        return fromValue

                //.compose(parseValue()) - decorator, is the same as:
                //=
                //.distinctUntilChanged()
                //.map(text -> priceParser.parse(text))

                .compose(parseValue())
                .switchMap(value -> factor.map(f -> value * f))
                .compose(formatValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //next 4 methods - to refresh data inside of a viewModel:

    final void fromCoin(int position) {
        fromPosition.onNext(position);
    }

    final void toCoin(int position) {
        toPosition.onNext(position);
    }

    final void fromValue(String text) {
        fromValue.onNext(text);
    }

    final void toValue(String text) {
        toValue.onNext(text);
    }

    @NonNull
    private ObservableTransformer<String, Double> parseValue() {
        /*
        return upstream -> upstream
                .distinctUntilChanged()
                .map(priceParser::parse);
        =
        return new ObservableTransformer<String, Double>() {
            @Override
            public ObservableSource<Double> apply(Observable<String> upstream) {
                return upstream.distinctUnitlChanged().map(text -> priceParser.parse(text));
            }

        */
        return upstream -> upstream
                .distinctUntilChanged()
                .map(priceParser::parse);
    }

    @NonNull
    private ObservableTransformer<Double, String> formatValue() {
        return upstream -> upstream.map(value -> {
            if (value > 0) {
                return priceFormatter.formatWithoutCurrencySymbol(value);
            } else {
                return "";
            }
        });
    }


}

package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

//from: [amount, coin]
//to:   [amount, coin]

class ConverterViewModel extends ViewModel {
    @Inject ConverterViewModel() {
        //[*]fromAmount -> [*]fromCoin -> [*]From
        //[*]toAmount -> [*]toCoin -> [*]To
        //[*]From -> [*]To -> [amount, amount]
    }

}

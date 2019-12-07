package com.loftschool.ozaharenko.loftcoin19.data;

import androidx.annotation.NonNull;

import com.loftschool.ozaharenko.loftcoin19.BuildConfig;
import com.loftschool.ozaharenko.loftcoin19.util.LogoUrlFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CmcLogoUrl implements LogoUrlFormatter {

    @Inject
    CmcLogoUrl() {
    }

    @NonNull
    @Override
    public String format(@NonNull Long value) {
        return BuildConfig.CMC_IMG_ENDPOINT + value + ".png";
    }
}

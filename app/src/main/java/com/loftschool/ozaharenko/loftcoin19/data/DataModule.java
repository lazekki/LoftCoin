package com.loftschool.ozaharenko.loftcoin19.data;

import com.loftschool.ozaharenko.loftcoin19.BuildConfig;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public abstract class DataModule {

    @Provides
    @Singleton
    static OkHttpClient httpClient() {
        final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        okHttpBuilder.addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                .addHeader(CmcApi.API_KEY, BuildConfig.CMC_API_KEY)
                .build()));

        //how to sent requests to log:
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            interceptor.redactHeader(CmcApi.API_KEY); //hide header with this value in the console log
            okHttpBuilder.addInterceptor(interceptor);
        }
        return okHttpBuilder.build();
    }

    @Provides
    @Singleton
    static Moshi moshi() {
        final Moshi moshi = new Moshi.Builder().build();
        return moshi.newBuilder()
                .add(Listings.class, moshi.adapter(AutoValue_Listings.class))
                .build();
    }

    @Provides
    @Singleton
    static Retrofit retrofit(OkHttpClient httpClient, Moshi moshi) {
        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BuildConfig.CMC_API_ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }

    @Provides
    @Singleton
    static CmcApi cmcApi(Retrofit retrofit) {
        return retrofit.create(CmcApi.class);
    }

}

package com.loftschool.ozaharenko.loftcoin19.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.loftschool.ozaharenko.loftcoin19.BuildConfig;
import com.loftschool.ozaharenko.loftcoin19.util.LogoUrlFormatter;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static CoinsDb coinsDb(Context context) {
/*
        There are 2 methods:
        Room.inMemoryDatabaseBuilder()
        AND
        Room.databaseBuilder()

        1st one is more preferable for DEBUG mode.
        Next code creates builder, what will contain one method for DEBUG (database in memory)
        and another one for Production build (database in the file)
*/
        final RoomDatabase.Builder<CoinsDb> builder;

        if (BuildConfig.DEBUG) {
            builder = Room.inMemoryDatabaseBuilder(context, CoinsDb.class);
        } else {
            builder = Room.databaseBuilder(context, CoinsDb.class, "coins.db");
        }
        return builder.build();
    }

    @Provides
    @Singleton
    static CmcApi cmcApi(Retrofit retrofit) {
        return retrofit.create(CmcApi.class);
    }

    @Binds
    abstract CurrencyRepo currencyRepo(DefaultCurrencyRepo impl);

    @Binds
    abstract CoinsRepo coinsRepo(CmcCoinsRepo impl);

    @Binds
    abstract LogoUrlFormatter logoUrlFormatter(CmcLogoUrl impl);

}

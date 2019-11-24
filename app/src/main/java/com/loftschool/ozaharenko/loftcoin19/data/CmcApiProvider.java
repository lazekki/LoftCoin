package com.loftschool.ozaharenko.loftcoin19.data;

import com.loftschool.ozaharenko.loftcoin19.BuildConfig;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Provider;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class CmcApiProvider implements Provider<CmcApi> {

    //volatile = switch off a cache of variables within a thread
    //keyword => Singleton Java pattern,
    /*Option 1: double check log (as pattern of Singleton implementation in Java)

    private static volatile CmcApi sApi;

    @Override
    public CmcApi get() {
        CmcApi api = sApi;
        if (api == null) {
            synchronized (CmcApiProvider.class) {
                api = sApi;
                if (api == null) {
                    api = sApi = new CmcApi() {
                        @Override
                        public Call<Listings> listings() {
                            return null;
                        }
                    };
                }
            }
        }
        return api;
    }*/

    //Option 2: Holder. Lazy singleton

    @Override
    public CmcApi get() {
        return Holder.API;
    }

    private static final class Holder {
        static final CmcApi API;

        static {
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

            final Moshi moshi = new Moshi.Builder().build();

            final Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpBuilder.build())
                    .baseUrl(BuildConfig.CMC_API_ENDPOINT)
                    .addConverterFactory(MoshiConverterFactory.create(moshi.newBuilder()
                            .add(Listings.class, moshi.adapter(AutoValue_Listings.class))
                            .build()))
                    .build();

            API = retrofit.create(CmcApi.class);
        }
    }
}

package com.loftschool.ozaharenko.loftcoin19.data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

//https://square.github.io/retrofit
public interface CmcApi {

    String API_KEY = "X-CMC_PRO_API_KEY";

    @GET("cryptocurrency/listings/latest")
    Observable<Listings> listings(@Query("convert") String convert);

}

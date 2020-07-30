package com.dkoptin.loftmoney.remote;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    // https://verdant-violet.glitch.me/items?type=expense
    @GET("./items")
    Single<MoneyResponse> getMoney(@Query("type") String type);

    // https://verdant-violet.glitch.me/items/add
    @POST("./items/add")
    @FormUrlEncoded
    Completable addMoney(@Field("price") String price,
                         @Field("name") String name,
                         @Field("type") String type);
}

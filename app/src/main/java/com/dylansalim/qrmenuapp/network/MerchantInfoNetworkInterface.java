package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.Rating;
import com.dylansalim.qrmenuapp.models.dao.Result;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MerchantInfoNetworkInterface {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/rating/create-rating")
    Observable<Result<Rating>> createRating(@Body Rating rating);

    @FormUrlEncoded
    @POST("/rating/get-all-rating-by-store-id")
    Observable<Result<List<Rating>>> getAllRating(@Field("store_id") int storeId);
}

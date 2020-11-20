package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface StoreRegistrationNetworkInterface {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/stores/create-store")
    Observable<Result<String>> createStore(@Body StoreDao storeDao);
}

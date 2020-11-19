package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MerchantRegistrationNetworkInterface {
    @FormUrlEncoded
    @POST("/stores/create-store")
    Observable<Result<String>> createStore(@Body StoreDao storeDao);
}

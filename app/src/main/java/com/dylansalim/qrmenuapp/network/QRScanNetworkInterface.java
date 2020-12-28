package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface QRScanNetworkInterface {

    @FormUrlEncoded
    @POST("/stores/get-store-by-user-id")
    Observable<Result<Store>> getStoreByUserId(@Field("userId") int userId);
}

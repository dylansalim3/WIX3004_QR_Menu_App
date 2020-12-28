package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StoreQRNetworkInterface {
    @FormUrlEncoded
    @POST("/stores/get-generated-qr-code")
    Observable<Result<String>> generateQRCode(@Field("storeId") int storeId);
}

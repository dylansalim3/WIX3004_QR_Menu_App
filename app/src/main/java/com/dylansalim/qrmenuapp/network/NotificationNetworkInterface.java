package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dto.FcmDto;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationNetworkInterface {

    @POST("/users/update-fcm")
    Observable<Result<String>> updateFCMToken(@Body FcmDto fcmDto);
}

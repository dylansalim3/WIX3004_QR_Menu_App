package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.NotificationDao;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dto.FcmDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificationNetworkInterface {

    @POST("/users/update-fcm")
    Observable<Result<String>> updateFCMToken(@Body FcmDto fcmDto);

    @FormUrlEncoded
    @POST("/notification/get-all-notifications")
    Observable<List<NotificationDao>> getNotifications(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("/notification/delete-all-notifications")
    Observable<Result<String>> deleteAllNotifications(@Field("user_id") int userId);
}

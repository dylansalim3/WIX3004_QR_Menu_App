package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.Notification;
import com.dylansalim.qrmenuapp.models.dto.Result;
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

    @POST("/notification/get-all-notifications")
    Observable<List<Notification>> getNotifications();

    @FormUrlEncoded
    @POST("/notification/read-notification")
    Observable<Result<String>> readNotification(@Field("id") int notificationId);

    @POST("/notification/delete-all-notifications")
    Observable<Result<String>> deleteAllNotifications();
}

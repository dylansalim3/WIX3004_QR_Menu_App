package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.models.dto.Token;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface StoreRegistrationNetworkInterface {

    @Multipart
    @POST("/stores/create-store")
    Observable<Result<Token>> createStore(
            @Part MultipartBody.Part image, @Part("name") RequestBody name, @Part("address") RequestBody address,
            @Part("postal_code") RequestBody postalCode, @Part("city") RequestBody city, @Part("country") RequestBody country,
            @Part("latitude") RequestBody latitude, @Part("longitude") RequestBody longitude, @Part("phone_num") RequestBody phoneNum,
            @Part("user_id") RequestBody userId, @Part("open_hour") RequestBody openHour, @Part("closing_hour") RequestBody closingHour,
            @Part("special_opening_note") RequestBody specialOpeningNote
    );

    @Multipart
    @POST("/stores/update-store")
    Observable<Result<Store>> updateStore(
            @Part MultipartBody.Part image, @Part("id") RequestBody id, @Part("name") RequestBody name, @Part("address") RequestBody address,
            @Part("postal_code") RequestBody postalCode, @Part("city") RequestBody city, @Part("country") RequestBody country,
            @Part("latitude") RequestBody latitude, @Part("longitude") RequestBody longitude, @Part("phone_num") RequestBody phoneNum,
            @Part("user_id") RequestBody userId, @Part("open_hour") RequestBody openHour, @Part("closing_hour") RequestBody closingHour,
            @Part("special_opening_note") RequestBody specialOpeningNote
    );
}

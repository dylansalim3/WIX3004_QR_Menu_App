package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.ImageUrl;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Token;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AccountNetworkInterface {

    @FormUrlEncoded
    @POST("/users/update-role")
    Observable<Token> updateRole(@Field("role") String role);

    @FormUrlEncoded
    @POST("/users/update-profile")
    Observable<Token> updateProfile(
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("phonenum") String phoneNum,
            @Field("address") String address
    );

    @Multipart
    @POST("/users/update-picture")
    Observable<Result<String>> updatePicture(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("/users/get-picture")
    Observable<ImageUrl> getImageUrl(@Field("user_id") int userId);
}

package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccountNetworkInterface {

    @FormUrlEncoded
    @POST("/users/update-role")
    Observable<Result<String>> updateRole(@Field("user_id") int userId, @Field("role") String role);

}

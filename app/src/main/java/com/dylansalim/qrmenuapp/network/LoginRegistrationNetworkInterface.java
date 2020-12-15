package com.dylansalim.qrmenuapp.network;


import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginRegistrationNetworkInterface {

    @FormUrlEncoded
    @POST("/users/login")
    Observable<TokenDao> submitLoginRequest(@Field(encoded = false, value = "email") String email, @Field("password") String password);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/users/register-user")
    Observable<TokenDao> submitRegistrationRequest(@Body RegistrationDto registrationDto);

    @GET("/roles/get-all-roles")
    Observable<List<RoleDao>> getRoles();

    @FormUrlEncoded
    @POST("/users/update-profile")
    Observable<TokenDao> updateProfile(
            @Field("userid") int userId,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("phonenum") String phoneNum,
            @Field("address") String address
    );
}

package com.dylansalim.qrmenuapp.network;


import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Token;
import com.dylansalim.qrmenuapp.models.dto.Role;
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
    Observable<Result<Token>> submitLoginRequest(@Field(encoded = false, value = "email") String email, @Field("password") String password);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/users/register-user")
    Observable<Result<Token>> submitRegistrationRequest(@Body RegistrationDto registrationDto);

    @GET("/roles/get-all-roles")
    Observable<List<Role>> getRoles();
}

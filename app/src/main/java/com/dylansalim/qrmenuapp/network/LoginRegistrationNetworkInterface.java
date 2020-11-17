package com.dylansalim.qrmenuapp.network;


import com.dylansalim.qrmenuapp.models.dao.LoginDao;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginRegistrationNetworkInterface {

    @FormUrlEncoded
    @POST("/users/login")
    Observable<LoginDao> submitLoginRequest(@Field(encoded = false, value = "email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/users/register-user")
    Observable<String> submitRegistrationRequest(@Body RegistrationDto registrationDto);

    @GET("/roles/get-all-roles")
    Observable<List<RoleDao>> getRoles();
}

package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Shop;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShopInterface {

    @GET("/favorite/favorite-list")
    Observable<Result<List<Shop>>> getFavoriteList(@Query("userId") String userId);

    @GET("/favorite/recently-store-list")
    Observable<Result<List<Shop>>> getRecentList(@Query("userId") String userId);
}

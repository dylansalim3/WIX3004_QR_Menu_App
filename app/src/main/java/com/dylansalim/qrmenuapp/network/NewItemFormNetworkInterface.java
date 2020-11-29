package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.ItemDao;
import com.dylansalim.qrmenuapp.models.dao.Result;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NewItemFormNetworkInterface {
    @FormUrlEncoded
    @POST("/items/get-item-by-id")
    Observable<Result<ItemDao>> getItemById(@Field("itemId") int itemId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/items/create-item")
    Observable<Result<ItemDao>> createItem(@Body ItemDao itemDao);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/items/update-item")
    Observable<Result<ItemDao>> updateItem(@Body ItemDao itemDao);

}

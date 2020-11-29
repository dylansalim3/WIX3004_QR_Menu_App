package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.AllItemDao;
import com.dylansalim.qrmenuapp.models.dao.ItemCategoryDao;
import com.dylansalim.qrmenuapp.models.dao.ItemDao;
import com.dylansalim.qrmenuapp.models.dao.Result;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MerchantItemNetworkInterface {

    @FormUrlEncoded
    @POST("/items/get-all-items-by-store-id")
    Observable<Result<List<AllItemDao>>> getAllItems(@Field("storeId") int storeId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/items/create-item")
    Observable<Result<List<ItemDao>>> createItem(@Body ItemDao itemDao);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/item-categories/create-item-category")
    Observable<Result<ItemCategoryDao>> createItemCategory(@Body ItemCategoryDao itemCategoryDao);

    @FormUrlEncoded
    @POST("/items/get-item-by-id")
    Observable<Result<List<ItemDao>>> getItemById(@Field("id") int id);
}

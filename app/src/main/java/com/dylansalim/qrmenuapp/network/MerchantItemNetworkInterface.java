package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.AllItemDao;
import com.dylansalim.qrmenuapp.models.dao.ItemCategoryDao;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;

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
    @POST("/item-categories/create-item-category")
    Observable<Result<ItemCategoryDao>> createItemCategory(@Body ItemCategoryDao itemCategoryDao);

    @FormUrlEncoded
    @POST("/stores/get-store-by-store-id")
    Observable<Result<StoreDao>> getStoreDetail(@Field("storeId") int storeId);

    @FormUrlEncoded
    @POST("/items/delete-item")
    Observable<Result<String>> deleteItem(@Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("/item-categories/delete-item-category")
    Observable<Result<String>> deleteItemCategory(@Field("item_category_id") int itemCategoryId);


}

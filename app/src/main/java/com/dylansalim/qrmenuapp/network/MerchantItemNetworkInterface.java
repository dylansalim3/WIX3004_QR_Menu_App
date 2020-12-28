package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.AllItem;
import com.dylansalim.qrmenuapp.models.dto.ItemCategory;
import com.dylansalim.qrmenuapp.models.dto.OverallRating;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;

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
    Observable<Result<List<AllItem>>> getAllItems(@Field("storeId") int storeId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/item-categories/create-item-category")
    Observable<Result<ItemCategory>> createItemCategory(@Body ItemCategory itemCategory);

    @FormUrlEncoded
    @POST("/stores/get-store-by-store-id")
    Observable<Result<Store>> getStoreDetail(@Field("storeId") int storeId);

    @FormUrlEncoded
    @POST("/stores/get-store-by-user-id")
    Observable<Result<Store>> getStoreDetailByUserId(@Field("userId") int userId);

    @FormUrlEncoded
    @POST("/items/delete-item")
    Observable<Result<String>> deleteItem(@Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("/item-categories/delete-item-category")
    Observable<Result<String>> deleteItemCategory(@Field("item_category_id") int itemCategoryId);

    @FormUrlEncoded
    @POST("/rating/get-average-rating-by-store-id")
    Observable<Result<OverallRating>> getRatingByStoreId(@Field("store_id") int storeId);

    @FormUrlEncoded
    @POST("/favorite/add-to-favorite")
    Observable<Result<String>> addToFavorite(@Field("userId") int userId,@Field("storeId") int storeId);

    @FormUrlEncoded
    @POST("/favorite/remove-favorite")
    Observable<Result<String>> removeFavorite(@Field("userId") int userId,@Field("storeId") int storeId);

    @FormUrlEncoded
    @POST("/favorite/add-to-recently-viewed")
    Observable<Result<String>> addToRecentlyViewed(@Field("userId") int userId,@Field("storeId") int storeId);

    @FormUrlEncoded
    @POST("/favorite/check-is-favorite")
    Observable<Result<Boolean>> checkIsFavorite(@Field("userId") int userId,@Field("storeId") int storeId);
}

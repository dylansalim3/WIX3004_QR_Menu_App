package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dao.ItemDao;
import com.dylansalim.qrmenuapp.models.dao.Result;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NewItemFormNetworkInterface {
    @FormUrlEncoded
    @POST("/items/get-item-by-id")
    Observable<Result<ItemDao>> getItemById(@Field("itemId") int itemId);

    @Multipart
    @POST("/items/create-item")
    Observable<Result<ItemDao>> createItem(@Part MultipartBody.Part image, @Part("item_category_id") RequestBody itemCategoryId,
                                           @Part("name") RequestBody name, @Part("desc") RequestBody desc,
                                           @Part("price") RequestBody price, @Part("promo_price") RequestBody promoPrice,
                                           @Part("hidden") RequestBody hidden, @Part("recommended") RequestBody recommended);

    @Multipart
    @POST("/items/update-item")
    Observable<Result<ItemDao>> updateItem(@Part MultipartBody.Part image,@Part("id") RequestBody id,
                                           @Part("name") RequestBody name, @Part("desc") RequestBody desc,
                                           @Part("price") RequestBody price, @Part("promo_price") RequestBody promoPrice,
                                           @Part("hidden") RequestBody hidden, @Part("recommended") RequestBody recommended);

}

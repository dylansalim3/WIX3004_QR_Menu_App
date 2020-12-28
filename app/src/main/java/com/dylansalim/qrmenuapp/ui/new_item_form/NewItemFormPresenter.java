package com.dylansalim.qrmenuapp.ui.new_item_form;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.models.dto.Item;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.NewItemFormNetworkInterface;
import com.dylansalim.qrmenuapp.utils.ValidationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NewItemFormPresenter implements NewItemFormPresenterInterface {
    private NewItemFormViewInterface nifvi;
    private boolean isNewForm;
    private static final String TAG = "nifp";
    private static final String CREATE_ITEM = "CREATE ITEM";
    private static final String UPDATE_ITEM = "UPDATE ITEM";
    private boolean recommended = false;
    private int categoryId;
    private int itemId;
    private boolean isPromo = false;
    private String itemImg;

    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();

    public NewItemFormPresenter(NewItemFormViewInterface nifvi) {
        this.nifvi = nifvi;
    }


    @Override
    public void retrieveItemDetail(int itemId) {
        isNewForm = false;
        this.itemId = itemId;
        nifvi.showProgressBar();
        disposableObservers.add(getNewItemFormNetworkInterface().getItemById(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getItemDetailObserver()));
    }

    @Override
    public void setCategoryId(int categoryId) {
        isNewForm = true;
        this.categoryId = categoryId;
    }

    @Override
    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    @Override
    public void setIsPromo(boolean isPromo) {
        if (!isPromo) {
            nifvi.setPromoPrice("");
        }
        this.isPromo = isPromo;
    }

    @Override
    public void onItemImageResult(Uri uri, ContentResolver contentResolver) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String itemImgUrl = cursor.getString(columnIndex);
        cursor.close();
        nifvi.setItemImage("file://" + itemImgUrl);


        itemImg = itemImgUrl;
        Log.d(TAG, itemImg);
    }

    @Override
    public void onSubmitForm() {
        if (isNewForm && isValidForm()) {
            submitForm(CREATE_ITEM);
        } else if (isValidForm()) {
            submitForm(UPDATE_ITEM);
        }
    }

    @Override
    public void disposeObserver() {
        for (DisposableObserver<?> disposableObserver : disposableObservers) {
            disposableObserver.dispose();
        }
    }

    private boolean isValidForm() {
        String itemName = nifvi.getItemName();
        String desc = nifvi.getDesc();
        String priceString = nifvi.getPrice();
        String promoPriceString = nifvi.getPromoPrice();
        String priceCurrency = nifvi.getPriceCurrency();

        List<String> nonNullFieldStrings = new ArrayList<>(Arrays.asList(itemName, desc, priceString, priceCurrency));
        if (isPromo) {
            nonNullFieldStrings.add(promoPriceString);
        }

        String[] strList = new String[nonNullFieldStrings.size()];
        strList = nonNullFieldStrings.toArray(strList);

        for (String nonNullFieldString : strList) {
            if (!ValidationUtils.isNonNullFieldValid(nonNullFieldString)) {
                nifvi.displayError("Cannot leave fields empty");
                return false;
            }
        }

        double price = Double.parseDouble(priceString);


        if (!ValidationUtils.isValidPrice(price)) {
            nifvi.displayError("Price need to be more than 0");
            return false;
        }
        if (promoPriceString.length() > 0) {
            double promoPrice = Double.parseDouble(promoPriceString);

            if (isPromo && !ValidationUtils.isValidPrice(promoPrice)) {
                nifvi.displayError("Promo Price need to be more than 0");
                return false;
            }

            if (isPromo && promoPrice >= price) {
                nifvi.displayError("Promo Price is more than price");
                return false;
            }
        }


        return true;
    }

    private void submitForm(String submitType) {
        String itemName = nifvi.getItemName();
        String desc = nifvi.getDesc();
        String priceCurrency = nifvi.getPriceCurrency();
        double price = Double.parseDouble(nifvi.getPrice());


        Item item = new Item();
        item.setItemCategoryId(categoryId);
        item.setName(itemName);
        item.setDesc(desc);
        item.setPrice(price);
        item.setCurrency(priceCurrency);
        item.setRecommended(recommended);

        RequestBody priceCurrencyBody = RequestBody.create(MediaType.parse("plain/text"), item.getCurrency());
        RequestBody nameBody = RequestBody.create(MediaType.parse("plain/text"), item.getName());
        RequestBody descBody = RequestBody.create(MediaType.parse("plain/text"), item.getDesc());
        RequestBody priceRequestBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(item.getPrice()));
        RequestBody promoPriceRequestBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(0));
        ;
        RequestBody hiddenRequestBody = RequestBody.create(MediaType.parse("plain/text"), Boolean.toString(false));
        RequestBody recommendedRequestBody = RequestBody.create(MediaType.parse("plain/text"), Boolean.toString(recommended));

        if (nifvi.getPromoPrice() != null && nifvi.getPromoPrice().length() > 0) {
            double promoPrice = Double.parseDouble(nifvi.getPromoPrice());
            item.setPromoPrice(promoPrice);
            promoPriceRequestBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(item.getPromoPrice()));
        }
        MultipartBody.Part imgBody = null;

        if (itemImg != null) {
            File file = new File(itemImg);

            RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file);

            imgBody = MultipartBody.Part.createFormData("file", "file.png", requestBody1);
        }

        if (CREATE_ITEM.equals(submitType)) {

            RequestBody itemDescriptionIdBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(item.getItemCategoryId()));
            disposableObservers.add(getNewItemFormNetworkInterface().createItem(imgBody, itemDescriptionIdBody, nameBody, descBody, priceRequestBody, promoPriceRequestBody, hiddenRequestBody, recommendedRequestBody, priceCurrencyBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getSubmitFormObserver(CREATE_ITEM)));
        } else if (UPDATE_ITEM.equals(submitType)) {
            RequestBody itemIdBody = RequestBody.create(MediaType.parse("plain/text"), String.valueOf(itemId));
            disposableObservers.add(getNewItemFormNetworkInterface().updateItem(imgBody, itemIdBody, nameBody, descBody, priceRequestBody, promoPriceRequestBody, hiddenRequestBody, recommendedRequestBody, priceCurrencyBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getSubmitFormObserver(UPDATE_ITEM)));
        }


    }

    public DisposableObserver<Result<Item>> getSubmitFormObserver(String type) {
        return new DisposableObserver<Result<Item>>() {
            @Override
            public void onNext(@NonNull Result<Item> itemDaoResult) {
                Log.d(TAG, "OnNext " + itemDaoResult);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                nifvi.displayError(e.toString());
                nifvi.hideProgressBar();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                nifvi.hideProgressBar();
                if (CREATE_ITEM.equals(type)) {
                    nifvi.onSuccessSubmission("Item created successfully");
                } else {
                    nifvi.onSuccessSubmission("Item updated successfully");
                }
            }
        };
    }

    private NewItemFormNetworkInterface getNewItemFormNetworkInterface() {
        return NetworkClient.getNetworkClient().create(NewItemFormNetworkInterface.class);
    }

    public DisposableObserver<Result<Item>> getItemDetailObserver() {
        return new DisposableObserver<Result<Item>>() {
            @Override
            public void onNext(@NonNull Result<Item> itemDaoResult) {
                if (itemDaoResult.getData() != null) {
                    setItemDataToView(itemDaoResult.getData());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                nifvi.displayError(e.toString());
                nifvi.hideProgressBar();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                nifvi.hideProgressBar();
            }
        };
    }

    private void setItemDataToView(Item item) {
        try {

            nifvi.setItemName(item.getName());
            nifvi.setDesc(item.getDesc());
            nifvi.setPrice(Double.toString(item.getPrice()));
            nifvi.setPriceCurrency(item.getCurrency());
            if (item.getPromoPrice() > 0) {
                setIsPromo(true);
                nifvi.setPromoPrice(Double.toString(item.getPromoPrice()));
            }
            recommended = item.isRecommmended();
            nifvi.setRecommended(item.isRecommmended());
        } catch (Exception err) {
            Log.e(TAG, err.toString());
        }
        if (item.getItemImg() != null && item.getItemImg().length() > 0) {
            nifvi.setItemImage(BuildConfig.SERVER_API_URL + "/" + item.getItemImg());
        }
    }
}

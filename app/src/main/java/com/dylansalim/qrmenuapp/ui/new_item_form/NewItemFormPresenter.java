package com.dylansalim.qrmenuapp.ui.new_item_form;

import android.util.Log;

import com.dylansalim.qrmenuapp.models.dao.ItemDao;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.NewItemFormNetworkInterface;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NewItemFormPresenter implements NewItemFormPresenterInterface {
    private NewItemFormViewInterface nifvi;
    private boolean isNewForm;
    private static final String TAG = "nifp";
    private boolean recommended = false;

    public NewItemFormPresenter(NewItemFormViewInterface nifvi) {
        this.nifvi = nifvi;
    }


    @Override
    public void retrieveItemDetail(int itemId) {
        isNewForm = false;
        nifvi.showProgressBar();
        getNewItemFormNetworkInterface().getItemById(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getItemDetailObserver());
    }

    @Override
    public void setCategoryId(int categoryId) {
        isNewForm = true;
    }

    @Override
    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    @Override
    public void submitForm() {
        if(isNewForm){
            //create
        }else{
            //update
        }
    }

    private NewItemFormNetworkInterface getNewItemFormNetworkInterface() {
        return NetworkClient.getNetworkClient().create(NewItemFormNetworkInterface.class);
    }

    public DisposableObserver<Result<ItemDao>> getItemDetailObserver() {
        return new DisposableObserver<Result<ItemDao>>() {
            @Override
            public void onNext(@NonNull Result<ItemDao> itemDaoResult) {
                Log.d(TAG, "OnNext " + itemDaoResult);
                if (itemDaoResult.getData() != null) {
                    setItemDataToView(itemDaoResult.getData());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                nifvi.hideProgressBar();
            }
        };
    }

    private void setItemDataToView(ItemDao itemDao) {
        nifvi.setItemName(itemDao.getName());
        nifvi.setDesc(itemDao.getDesc());
        nifvi.setPrice(Double.toString(itemDao.getPrice()));
        nifvi.setPromoPrice(Double.toString(itemDao.getPromoPrice()));
    }
}

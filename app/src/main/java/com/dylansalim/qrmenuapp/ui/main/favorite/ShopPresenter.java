package com.dylansalim.qrmenuapp.ui.main.favorite;

import android.util.Log;

import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Shop;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.ShopInterface;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShopPresenter {

    private IShopView iShopView;
    private List<Shop> favoriteList;

    public static final String TAG = "ShopPresenter";

    public ShopPresenter(IShopView iShopView) {
        this.iShopView = iShopView;
    }

    private ShopInterface getBaseClientService() {
        return NetworkClient.getNetworkClient().create(ShopInterface.class);
    }

    public void onDestroy() {
        iShopView = null;
    }

    public List<Shop> getFavoriteList(){
        return favoriteList;
    }

    /**
     * 收藏shop
     *
     * @param userId user id
     */
    public void getFavoriteList(String userId) {
        getBaseClientService().getFavoriteList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<Shop>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<Shop>> result) {
                        Log.d(TAG, "get shop list success");
                        if (result == null) {
                            return;
                        }
                        if (iShopView != null && result.getData()!=null) {
                            favoriteList = result.getData();
                            iShopView.onGetShopList(favoriteList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "get shop list error:" + e);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 主页shop
     *
     * @param userId user id
     */
    public void getRecentList(String userId) {
        getBaseClientService().getRecentList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<Shop>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<Shop>> result) {
                        Log.d(TAG, "get shop list success");
                        if (result == null) {
                            return;
                        }
                        if (iShopView != null) {
                            iShopView.onGetShopList(result.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "get shop list error:" + e);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

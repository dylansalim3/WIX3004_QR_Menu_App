package com.dylansalim.qrmenuapp.ui.store_qr;

import android.graphics.Bitmap;
import android.util.Log;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.StoreQRNetworkInterface;
import com.dylansalim.qrmenuapp.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class StoreQRPresenter implements StoreQRPresenterInterface {
    private StoreQRViewInterface sqvi;
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();
    private String qrBase64Result;


    private Store storeDetail;

    public StoreQRPresenter(StoreQRViewInterface sqvi) {
        this.sqvi = sqvi;
    }

    @Override
    public void setStoreDetail(Store storeDetail) {
        if (null != storeDetail) {
            Log.d("sqrp", "qr result ");
            this.storeDetail = storeDetail;
            disposableObservers.add(getStoreQRNetworkInterface().generateQRCode(storeDetail.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getQRCodeObserver()));
        }
    }

    @Override
    public void onShareBtnClick() {
        if (null != qrBase64Result && null != storeDetail) {
            Bitmap bitmap = BitmapUtils.base64ToBitmapConverter(qrBase64Result);
            String desc = "Checkout this store! Scan the QR Code to view the store now!";
            sqvi.shareQR(bitmap, desc);
        }
    }

    public DisposableObserver<Result<String>> getQRCodeObserver() {
        return new DisposableObserver<Result<String>>() {
            @Override
            public void onNext(@NonNull Result<String> result) {
                qrBase64Result = result.getData();
                Bitmap bitmap = BitmapUtils.base64ToBitmapConverter(result.getData());
                sqvi.setQRDetail(bitmap, storeDetail.getName(), storeDetail.getAddress(), BuildConfig.SERVER_API_URL + "/" + storeDetail.getProfileImg());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //show err
            }

            @Override
            public void onComplete() {
                //hide progress base
            }
        };
    }

    private StoreQRNetworkInterface getStoreQRNetworkInterface() {
        return NetworkClient.getNetworkClient().create(StoreQRNetworkInterface.class);
    }

    @Override
    public void disposeObserver() {
        for (DisposableObserver<?> disposableObserver : disposableObservers) {
            disposableObserver.dispose();
        }
    }

}

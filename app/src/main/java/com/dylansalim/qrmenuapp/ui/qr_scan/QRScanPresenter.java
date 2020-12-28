package com.dylansalim.qrmenuapp.ui.qr_scan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.models.dto.UserDetail;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.QRScanNetworkInterface;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class QRScanPresenter implements QRScanPresenterInterface {

    private final String TAG = "QRScanPresenter";
    private QRScanViewInterface qsvi;
    private String roleName;
    private Context mContext;
    private String token;
    public String userName;

    public QRScanPresenter(QRScanViewInterface qsvi) {
        this.qsvi = qsvi;
    }

    @Override
    public void checkUserType(Activity activity) {
        if (token == null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.app_name),Context.MODE_PRIVATE);
            token = sharedPreferences.getString(activity.getString(R.string.token), "");
        }
        Log.d(TAG, "check user type");
        Log.d(TAG, token);

        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserDetail userDetail = new Gson().fromJson(dataString, UserDetail.class);
            Log.d(TAG, userDetail.toString());
            int userId = userDetail.getId();
            roleName = userDetail.getRole();
            userName = userDetail.getFirstName() + " " + userDetail.getLastName();

            getQRScanNetworkClient().getStoreByUserId(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getStoreObserver());
        }
    }

    @Override
    public void setToken(String token) {
        if (token != null) {
            this.token = token;
        }
    }

    public String getUserName(){
        return userName;
    }

    private QRScanNetworkInterface getQRScanNetworkClient() {
        return NetworkClient.getNetworkClient().create(QRScanNetworkInterface.class);
    }

    public DisposableObserver<Result<Store>> getStoreObserver() {
        return new DisposableObserver<Result<Store>>() {

            @Override
            public void onNext(Result<Store> storeResult) {
                if (roleName.equalsIgnoreCase("MERCHANT") && storeResult.getData() == null) {
                    qsvi.showStoreNotFoundAlert();
                }else if(roleName.equalsIgnoreCase("MERCHANT")){
                }
            }

            @Override
            public void onError(Throwable e) {
                qsvi.displayError("Error occurred");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
            }
        };
    }
}

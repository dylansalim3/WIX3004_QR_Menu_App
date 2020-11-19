package com.dylansalim.qrmenuapp.ui.QRScan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;


import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.QRScanNetworkInterface;
import com.dylansalim.qrmenuapp.ui.store_registration.StoreRegistrationActivity;
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

    public QRScanPresenter(QRScanViewInterface qsvi, Context mContext) {
        this.qsvi = qsvi;
        this.mContext = mContext;
    }

    @Override
    public void checkUserType(Activity activity) {
        if(token==null){
            SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
            token = sharedPreferences.getString(activity.getString(R.string.token), "");
        }
        Log.d(TAG,"check user type");
        Log.d(TAG, token);

        if(token != null){
            String dataString = null;
            try {
                if (BuildConfig.DEBUG && token.equals("")) {
                    throw new AssertionError("Assertion failed");
                }
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
            Log.d(TAG, userDetailDao.toString());
            int userId = userDetailDao.getId();
            roleName = userDetailDao.getRole();

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


    private QRScanNetworkInterface getQRScanNetworkClient() {
        return NetworkClient.getNetworkClient().create(QRScanNetworkInterface.class);
    }

    public DisposableObserver<Result<StoreDao>> getStoreObserver() {
        return new DisposableObserver<Result<StoreDao>>() {

            @Override
            public void onNext(Result<StoreDao> storeResult) {
                if (roleName.equalsIgnoreCase("MERCHANT") && storeResult.getData() == null) {
                    showStoreNotFoundAlert();
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

    public void showStoreNotFoundAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Setup Store?");

        // Setting Dialog Message
        alertDialog.setMessage("You have not setup a store yet. Setup now?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Go to Registration", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                qsvi.navigateToNextScreen(StoreRegistrationActivity.class);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

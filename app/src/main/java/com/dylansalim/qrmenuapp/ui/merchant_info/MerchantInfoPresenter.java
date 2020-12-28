package com.dylansalim.qrmenuapp.ui.merchant_info;

import android.content.Context;
import android.util.Log;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.models.dto.Rating;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.models.dto.UserDetail;
import com.dylansalim.qrmenuapp.network.MerchantInfoNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.services.SessionService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MerchantInfoPresenter implements MerchantInfoPresenterInterface {

    private MerchantInfoViewInterface mivi;
    private Store storeDetail;
    private boolean isStoreAdmin;
    private static final String TAG = "mipi";
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();


    public MerchantInfoPresenter(MerchantInfoViewInterface mivi) {
        this.mivi = mivi;
    }


    @Override
    public void setStoreInfo(Store storeDetail, boolean isStoreAdmin) {
        Log.d(TAG, storeDetail.toString());
        this.storeDetail = storeDetail;
        mivi.setupToolbar(storeDetail.getName());
        if (storeDetail.getProfileImg() != null) {
            mivi.setProfileImg(BuildConfig.SERVER_API_URL + "/" + storeDetail.getProfileImg());
        }
        this.isStoreAdmin = isStoreAdmin;
    }

    @Override
    public void setupFragments(List<Fragment> fragments) {
        if (storeDetail != null) {
            mivi.populateView(fragments, storeDetail);
        }
    }

    @Override
    public void onQRActionBtnClick() {
        // post req to retrieve qr
        if (null != storeDetail) {
            mivi.navigateToStoreQRActivity(storeDetail);
        }
    }

    @Override
    public void onEditBtnClick() {
        if (null != storeDetail) {
            mivi.navigateToEditStoreActivity(storeDetail);
        }
    }

    @Override
    public void onReportBtnClick() {
        if (null != storeDetail) {
            mivi.navigateToReportActivity(storeDetail);
        }
    }

    @Override
    public void onReviewSubmit(String text, float rating, Context context) {
        mivi.showProgressBar();
        UserDetail userDetail = SessionService.getUserDetails(context);
        if (null != storeDetail && null != SessionService.getUserDetails(context)) {
            int storeId = storeDetail.getId();
            int userId = userDetail.getId();
            Rating ratingModel = new Rating(storeId, userId, rating, text);
            disposableObservers.add(getMerchantInfoNetworkInterface().createRating(ratingModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getSubmitRatingObserver()));
        }
    }

    public DisposableObserver<Result<Rating>> getSubmitRatingObserver() {
        return new DisposableObserver<Result<Rating>>() {
            @Override
            public void onNext(@NonNull Result<Rating> ratingResult) {
                mivi.displayToast("Review added");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                mivi.displayToast(e.toString());
            }

            @Override
            public void onComplete() {
                mivi.hideProgressBar();
            }
        };
    }

    private MerchantInfoNetworkInterface getMerchantInfoNetworkInterface() {
        return NetworkClient.getNetworkClient().create(MerchantInfoNetworkInterface.class);
    }

    @Override
    public void disposeObserver() {
        for (DisposableObserver<?> disposableObserver : disposableObservers) {
            disposableObserver.dispose();
        }
    }

}

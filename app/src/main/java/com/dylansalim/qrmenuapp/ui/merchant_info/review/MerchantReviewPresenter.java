package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import com.dylansalim.qrmenuapp.models.RatingListItem;
import com.dylansalim.qrmenuapp.models.dto.Rating;
import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.network.MerchantInfoNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MerchantReviewPresenter implements MerchantReviewPresenterInterface {

    private MerchantReviewViewInterface mrvi;
    private Store storeDetail;
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();


    public MerchantReviewPresenter(MerchantReviewViewInterface mrvi) {
        this.mrvi = mrvi;
    }

    @Override
    public void setStoreDetail(Store storeDetail) {
        this.storeDetail = storeDetail;
        if (null != storeDetail) {
            disposableObservers.add(getMerchantInfoNetworkInterface().getAllRating(storeDetail.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getAllRatingObserver()));
        }

    }

    public DisposableObserver<Result<List<Rating>>> getAllRatingObserver() {
        return new DisposableObserver<Result<List<Rating>>>() {
            @Override
            public void onNext(@NonNull Result<List<Rating>> ratingListResult) {
                List<Rating> ratingList = ratingListResult.getData();
                if (null != ratingList) {
                    List<RatingListItem> ratingListItems = ratingList.stream()
                            .map(rating -> new RatingListItem(rating.getUsername(), rating.getCreated(), rating.getDesc(), rating.getRating()))
                            .collect(Collectors.toList());
                    mrvi.fillRecyclerView(ratingListItems);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
//                mrvi.displayError(e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private MerchantInfoNetworkInterface getMerchantInfoNetworkInterface() {
        return NetworkClient.getNetworkClient().create(MerchantInfoNetworkInterface.class);
    }

}

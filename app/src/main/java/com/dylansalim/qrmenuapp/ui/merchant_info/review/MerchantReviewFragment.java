package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylansalim.qrmenuapp.R;

import androidx.fragment.app.Fragment;

public class MerchantReviewFragment extends Fragment implements MerchantReviewViewInterface {

    public static final int FRAGMENT_INDEX = 1;
    private MerchantReviewPresenterInterface merchantReviewPresenterInterface;

    public MerchantReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMVP();
        Bundle bundle = getArguments();
        if (null != bundle && null != bundle.getParcelable(getResources().getString(R.string.store_result))) {
            merchantReviewPresenterInterface.setStoreDetail(bundle.getParcelable(getResources().getString(R.string.store_result)));
        }
    }

    private void setupMVP() {
        merchantReviewPresenterInterface = new MerchantReviewPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_review, container, false);

        // setup recycler view & retrieve rating data from backend

        return view;
    }
}
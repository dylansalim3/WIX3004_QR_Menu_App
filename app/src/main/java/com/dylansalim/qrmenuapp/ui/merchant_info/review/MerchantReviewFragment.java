package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylansalim.qrmenuapp.R;

import androidx.fragment.app.Fragment;

public class MerchantReviewFragment extends Fragment {

    public static final int FRAGMENT_INDEX = 1;

    public MerchantReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_merchant_review, container, false);
    }
}
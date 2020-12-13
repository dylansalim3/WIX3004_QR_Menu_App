package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.RatingListItem;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MerchantReviewFragment extends Fragment implements MerchantReviewViewInterface {

    public static final int FRAGMENT_INDEX = 1;
    private MerchantReviewPresenterInterface merchantReviewPresenterInterface;
    private ReviewArrayAdapter reviewArrayAdapter;
    private TextView mEmptyListTv;
    private RecyclerView mRecyclerView;

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
        mRecyclerView = view.findViewById(R.id.recycler_view_merchant_review);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mEmptyListTv = view.findViewById(R.id.tv_merchant_review_empty_list);

        reviewArrayAdapter = new ReviewArrayAdapter();
        mRecyclerView.setAdapter(reviewArrayAdapter);

        return view;
    }


    @Override
    public void fillRecyclerView(List<RatingListItem> ratingListItemList) {
        if (null != reviewArrayAdapter) {
            reviewArrayAdapter.fillData(ratingListItemList);

            if (ratingListItemList.size() > 0) {
                mEmptyListTv.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }else{
                mRecyclerView.setVisibility(View.GONE);
                mEmptyListTv.setVisibility(View.VISIBLE);
            }
        }
    }
}
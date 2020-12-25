package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import com.dylansalim.qrmenuapp.models.RatingListItem;

import java.util.List;

public interface MerchantReviewViewInterface {
    void fillRecyclerView(List<RatingListItem> ratingListItemList);
}

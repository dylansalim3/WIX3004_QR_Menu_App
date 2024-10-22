package com.dylansalim.qrmenuapp.ui.merchant;

import com.dylansalim.qrmenuapp.models.EditListItem;
import com.dylansalim.qrmenuapp.models.dto.Store;

import java.util.List;

public interface MerchantViewInterface {
    void displayError(String s);

    void showProgressBar();

    void hideProgressBar();

    void setOverallRating(String overallRating);

    void setupRecyclerView(List<EditListItem> editListItems);

    void setupTabLayout(List<String> title);

    void scrollRecyclerViewToPosition(int position);

    void updateSelectedTab(int position);

    void setupToolbarTitle(String title);

    void updateEditActionIcon(int drawableId);

    void updateFavActionIcon(int drawableId);

    void showAddNewCategoryDialog();

    void navigateToMerchantInfoActivity(Store storeResult, boolean isStoreAdmin);

    void navigateToStoreQRActivity(Store storeResult);

    void setProfileImg(String profileImg);
}

package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Context;

import androidx.annotation.Nullable;

public interface MerchantPresenterInterface {
    void getAllItems(Context context, @Nullable Integer storeId);

    void retrieveItemDetail();

    void retrieveStoreDetail(@Nullable Boolean retrieveItem);

    void onTabSelectionChanged(int tabIndex);

    void onRecyclerViewScrolled(int recyclerViewItemIndex);

    void onEditActionButtonClick();

    void onFavActionButtonClick();

    void onShareBtnClick();

    void onAddNewCategory(String categoryName);

    void onDeleteItem(int itemId);

    void onDeleteItemCategory(int itemCategoryId);

    void onInfoButtonClick();

    void disposeObserver();
}

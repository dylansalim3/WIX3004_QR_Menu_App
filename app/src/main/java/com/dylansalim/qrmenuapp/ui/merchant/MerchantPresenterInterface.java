package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Context;

import androidx.annotation.Nullable;

public interface MerchantPresenterInterface {
    void getAllItems(Context context, @Nullable Integer storeId);

    void retrieveItemDetail();

    void retrieveStoreDetail();

    void onTabSelectionChanged(int tabIndex);

    void onRecyclerViewScrolled(int recyclerViewItemIndex);

    void onEditActionButtonClicked();

    void onAddNewCategory(String categoryName);

    void onDeleteItem(int itemId);

    void onDeleteItemCategory(int itemCategoryId);

    void disposeObserver();
}

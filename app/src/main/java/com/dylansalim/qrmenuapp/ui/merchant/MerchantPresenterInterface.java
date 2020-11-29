package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Context;

public interface MerchantPresenterInterface {
    void getAllItems(Context context);

    void onTabSelectionChanged(int tabIndex);

    void onRecyclerViewScrolled(int recyclerViewItemIndex);

    void onEditActionButtonClicked();

    void onAddNewCategory(String categoryName);
}

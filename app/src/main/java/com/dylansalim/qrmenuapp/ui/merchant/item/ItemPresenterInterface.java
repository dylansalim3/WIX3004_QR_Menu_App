package com.dylansalim.qrmenuapp.ui.merchant.item;

import android.content.Context;

public interface ItemPresenterInterface {
    void getAllItems(Context context);

    void onTabSelectionChanged(int tabIndex);

    void onRecyclerViewScrolled(int recyclerViewItemIndex);
}

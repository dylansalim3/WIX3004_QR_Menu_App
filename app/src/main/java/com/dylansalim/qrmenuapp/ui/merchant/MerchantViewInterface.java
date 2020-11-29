package com.dylansalim.qrmenuapp.ui.merchant;

import com.dylansalim.qrmenuapp.models.ListItem;

import java.util.List;

public interface MerchantViewInterface {
    void displayError(String s);

    void setupRecyclerView(List<ListItem> listItems);

    void setupTabLayout(List<String> title);

    void scrollRecyclerViewToPosition(int position);

    void updateSelectedTab(int position);

    void setupToolbarTitle(String title);

    void updateEditActionIcon(int drawableId);

    void showAddNewCategoryDialog();

    void showProgressBar();

    void hideProgressBar();
}

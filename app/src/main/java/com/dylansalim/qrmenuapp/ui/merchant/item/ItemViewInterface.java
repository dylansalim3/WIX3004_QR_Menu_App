package com.dylansalim.qrmenuapp.ui.merchant.item;

import com.dylansalim.qrmenuapp.models.ListItem;

import java.util.List;

public interface ItemViewInterface {
    void setupRecyclerView(List<ListItem> listItems);

    void setupTabLayout(List<String> title);

    void scrollRecyclerViewToPosition(int position);

    void updateSelectedTab(int position);
}

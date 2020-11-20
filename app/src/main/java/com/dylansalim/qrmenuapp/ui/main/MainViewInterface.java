package com.dylansalim.qrmenuapp.ui.main;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface MainViewInterface {
    void populateBottomNavBar(@Nullable Integer itemId);

    void populateFragmentAdapter(List<Fragment> fragments);

    void showFab();

    void setViewPagerIndex(int index);

    void setSelectedBottomNavBar(int itemId);
}
